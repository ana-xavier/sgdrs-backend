package br.com.sgdrs.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import com.opencsv.CSVWriter;

import br.com.sgdrs.controller.request.EditarItemRequest;
import br.com.sgdrs.controller.response.EditarItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.sgdrs.controller.request.EstoqueRequest;
import br.com.sgdrs.controller.request.EstoqueItem;
import br.com.sgdrs.controller.response.ItemResponse;
import br.com.sgdrs.controller.response.ItemVerificadoResponse;
import br.com.sgdrs.controller.response.RelatorioResponse;
import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.domain.Doacao;
import br.com.sgdrs.domain.Doador;
import br.com.sgdrs.domain.Item;
import br.com.sgdrs.domain.ProdutoDoacao;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.domain.Relatorio.Relatorio;
import br.com.sgdrs.mapper.ItemMapper;
import br.com.sgdrs.mapper.ItemVerificadoMapper;
import br.com.sgdrs.mapper.RelatorioMapper;
import br.com.sgdrs.repository.DoacaoRepository;
import br.com.sgdrs.repository.DoadorRepository;
import br.com.sgdrs.repository.ItemRepository;
import br.com.sgdrs.repository.ProdutoDoacaoRepository;
import br.com.sgdrs.service.util.BuscarUsuarioLogadoService;
import jakarta.transaction.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.coderion.model.Product;
import pl.coderion.model.ProductResponse;
import pl.coderion.service.OpenFoodFactsWrapper;
import pl.coderion.service.impl.OpenFoodFactsWrapperImpl;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class EstoqueService {
    private static final String ITEM_NAO_ENCONTRADO = "O item buscado não existe!";
    private static final String EDICAO_VOLUNTARIO_OK = "Edição enviada com sucesso para análise!";
    private static final String EDICAO_ADMIN_OK = "Item aprovado e registrado com sucesso!";
    private static final String EDICAO_ADMIN_INCREMENTO = "O item editado já existe. Sua quantidade foi incrementada em 1!";
    @Autowired
    private ItemRepository itemRepository;

    @Autowired 
    private DoadorRepository doadorRepository;

    @Autowired
    private DoacaoRepository doacaoRepository;


    @Autowired
    private ProdutoDoacaoRepository produtoDoacaoRepository;

    @Autowired
    private BuscarUsuarioLogadoService buscarUsuarioLogadoService;

    private final OpenFoodFactsWrapper openFoodFactsWrapper = new OpenFoodFactsWrapperImpl();

    @Transactional
    public ItemVerificadoResponse verificar(String codigoProduto) {
        if(isBlank(codigoProduto)){
            return ItemVerificadoResponse.builder()
                    .item(null)
                    .status(false)
                    .build();
        }
        Usuario usuarioLogado = buscarUsuarioLogadoService.getLogado();
        CentroDistribuicao centroDistribuicao = usuarioLogado.getCentroDistribuicao();

        Optional<Item> itemExistente = itemRepository.findByCodBarrasAndCentroDistribuicao(codigoProduto, centroDistribuicao);

        if (itemExistente.isPresent()) {
            return ItemVerificadoMapper.toResponse(itemExistente.get(), true);
        }

        ProductResponse productResponse = openFoodFactsWrapper.fetchProductByCode(codigoProduto);
        if (productResponse != null && productResponse.getProduct() != null) {
            Product product = productResponse.getProduct();
            String medida = product.getQuantity();
            String unidadeMedida = "";
            int valorMedida = 0;

            if(medida.contains(" ")){
                 unidadeMedida = product.getQuantity().split(" ")[1];
                 valorMedida = Integer.parseInt(product.getQuantity().split(" ")[0]);
            }else{
                int i = 0;
                while (i < medida.length() && Character.isDigit(medida.charAt(i))) {
                    i++;
                }
                valorMedida = Integer.parseInt(medida.substring(0, i));
                unidadeMedida = medida.substring(i);
            }

            String[] categorias = product.getCategoriesHierarchy();
            String ultimaCategoria = "";

            if (categorias.length > 0) {
                ultimaCategoria = categorias[categorias.length - 1].split(":")[1];
            }
            
        
            Item novoItem = Item.builder()
                .codBarras(codigoProduto)
                .nome(product.getProductName())
                .quantidade(0)
                .categoria(ultimaCategoria)
                .valorMedida(valorMedida)
                .unidadeMedida(unidadeMedida)
                .centroDistribuicao(centroDistribuicao)
                .validado(true)
                .build();
            itemRepository.save(novoItem);

            return ItemVerificadoMapper.toResponse(novoItem, true);
        }


        Item itemInvalido = Item.builder()
                .codBarras(codigoProduto)
                .nome("")
                .quantidade(0)
                .descricao("")
                .categoria("")
                .valorMedida(0)
                .unidadeMedida("")
                .centroDistribuicao(centroDistribuicao)
                .validado(false)
                .build();
        itemRepository.save(itemInvalido);
        return ItemVerificadoMapper.toResponse(itemInvalido, false);
    }
    @Transactional
    public List<ItemResponse> cadastrarItens(EstoqueRequest request){
        Usuario usuarioLogado = buscarUsuarioLogadoService.getLogado();
        CentroDistribuicao centroDistribuicao = usuarioLogado.getCentroDistribuicao();
        List<ItemResponse> response = new ArrayList<ItemResponse>();

        List<EstoqueItem> itens = request.getItens();
        Doador doador = doadorRepository.findById(request.getId_doador())
              .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Doador Não Encontrado"));

        Doacao doacao = Doacao.builder()
          .centroDistribuicao(centroDistribuicao)
          .data(LocalDate.now())
          .doador(doador)
          .build();
          doacaoRepository.save(doacao);

        for (EstoqueItem estoqueItem : itens) {
            Optional<Item> itemExistente = itemRepository.findByCodBarrasAndCentroDistribuicao(estoqueItem.getCodBarras(),centroDistribuicao);
            Item itemAtualizado = itemExistente.get();
            itemAtualizado.setQuantidade(itemAtualizado.getQuantidade() + estoqueItem.getQuantidade());
            itemRepository.save(itemAtualizado);

            ProdutoDoacao produtoDoacao = ProdutoDoacao.builder()
                .doacao(doacao)
                .item(itemAtualizado)
                .quantidade(estoqueItem.getQuantidade())
                .build();
            produtoDoacaoRepository.save(produtoDoacao);
            response.add(ItemMapper.toResponse(itemAtualizado));
        }

        return response;
    }

    public List<ItemResponse> listarItensNaoValidados() {
        Usuario solicitante = buscarUsuarioLogadoService.getLogado();

        return itemRepository.findByValidadoAndCentroDistribuicao(false, solicitante.getCentroDistribuicao()).stream()
                .map(ItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    public EditarItemResponse editarItem(UUID idItem, EditarItemRequest request) {
        Usuario solicitante = buscarUsuarioLogadoService.getLogado();
        Item editado = itemRepository.findByIdAndCentroDistribuicao(idItem, solicitante.getCentroDistribuicao())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, ITEM_NAO_ENCONTRADO));

        editado.setNome(request.getNome());
        editado.setCategoria(request.getCategoria());
        editado.setDescricao(request.getDescricao());
        editado.setValorMedida(request.getValorMedida());
        editado.setUnidadeMedida(request.getUnidadeMedida());

        itemRepository.save(editado);
        return EditarItemResponse.builder()
                .status(EDICAO_VOLUNTARIO_OK)
                .build();
    }

    @Transactional
    public EditarItemResponse aprovarItem(UUID idItem,UUID id_doador, EditarItemRequest request) {
        Usuario solicitante = buscarUsuarioLogadoService.getLogado();
        Item editado = itemRepository.findByIdAndCentroDistribuicao(idItem, solicitante.getCentroDistribuicao())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, ITEM_NAO_ENCONTRADO));


        Doador doador = doadorRepository.findById(id_doador)
              .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Doador Não Encontrado"));

        Doacao doacao = Doacao.builder()
         .centroDistribuicao(solicitante.getCentroDistribuicao())
         .data(LocalDate.now())
         .doador(doador)
         .build();
         doacaoRepository.save(doacao);

        Optional<Item> itemBuscado = itemRepository.encontrarPorNomeCategoriaEIdDiferente(editado.getNome(), editado.getCategoria(), idItem);
        if(itemBuscado.isPresent()){
            Item itemBuscadoObj = itemBuscado.get();
            itemBuscadoObj.setQuantidade(itemBuscadoObj.getQuantidade() + request.getQuantidade());
            itemRepository.save(itemBuscadoObj);
            itemRepository.deleteById(idItem);

            ProdutoDoacao produtoDoacao = ProdutoDoacao.builder()
            .doacao(doacao)
            .item(itemBuscadoObj)
            .quantidade(request.getQuantidade())
            .build();
            produtoDoacaoRepository.save(produtoDoacao);

            return EditarItemResponse.builder()
                    .status(EDICAO_ADMIN_INCREMENTO)
                    .build();
        }
        editado.setNome(isBlank(request.getNome()) ? editado.getNome() : request.getNome());
        editado.setCategoria(isBlank(request.getCategoria()) ? editado.getCategoria() : request.getCategoria());
        editado.setDescricao(isBlank(request.getDescricao()) ? editado.getDescricao() : request.getDescricao());
        editado.setValorMedida(request.getValorMedida());
        editado.setUnidadeMedida(isBlank(request.getUnidadeMedida()) ? editado.getUnidadeMedida() : request.getUnidadeMedida());
        editado.setValidado(true);

        itemRepository.save(editado);

        ProdutoDoacao produtoDoacao = ProdutoDoacao.builder()
           .doacao(doacao)
           .item(editado)
           .quantidade(request.getQuantidade())
           .build();  
        produtoDoacaoRepository.save(produtoDoacao);
        
        return EditarItemResponse.builder()
                .status(EDICAO_ADMIN_OK)
                .build();
    }

    public Doador IncluirDoador(Doador doadorRequest){
        if(doadorRequest == null){
            Doador doador = Doador.builder()
               .nome("Anonimo")
               .cpfCnpj("xxxxxxxxxxx")
               .build();
            doadorRepository.save(doador);
            return doador;
        }
        Doador doador = Doador.builder()
              .nome(doadorRequest.getNome())
              .cpfCnpj(doadorRequest.getCpfCnpj())
              .build();

        doadorRepository.save(doador);
        return doador;
    }

    public  RelatorioResponse exportarRelatorioDoacao(UUID id_centro) throws IOException{
        List<Relatorio> relatorioList = doadorRepository.findRelatorioByCentroId(id_centro);

        String caminhoArquivo = "./data/relatorio_doacao.csv";
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(caminhoArquivo))) {
            
            // Cabeçalho do CSV
            String[] cabecalho = {"Nome Doador", "CPF/CNPJ Doador", "Nome Item", "Categoria Item", 
                                  "Valor por Unidade", "Unidade Item", "Código de Barras", "Quantidade Doada","Data"};
            writer.writeNext(cabecalho);

            // Preenche os dados no CSV
            for (Relatorio relatorio : relatorioList) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
                String formattedDate = relatorio.getData().format(formatter);

                String[] dados = {
                        relatorio.getNome_doador(),
                        relatorio.getCpf_cnpj_doador(),
                        relatorio.getNome_item(),
                        relatorio.getCategoria_item(),
                        String.valueOf(relatorio.getValor_por_unidade()),
                        relatorio.getUnidade_item(),
                        relatorio.getCod_barras_item(),
                        String.valueOf(relatorio.getQuantidade_doada()),
                        formattedDate                                 
                };
                writer.writeNext(dados);
            }
        }

        return RelatorioMapper.toResponse(caminhoArquivo);
        
    }
}
