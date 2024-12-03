package br.com.sgdrs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.sgdrs.controller.request.EditarItemRequest;
import br.com.sgdrs.controller.response.EditarItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.sgdrs.controller.request.EstoqueRequest;
import br.com.sgdrs.controller.request.EstoqueItem;
import br.com.sgdrs.controller.response.ItemResponse;
import br.com.sgdrs.controller.response.ItemVerificadoResponse;
import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.domain.Item;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.mapper.ItemMapper;
import br.com.sgdrs.mapper.ItemVerificadoMapper;
import br.com.sgdrs.repository.ItemRepository;
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
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BuscarUsuarioLogadoService buscarUsuarioLogadoService;

    private final OpenFoodFactsWrapper openFoodFactsWrapper = new OpenFoodFactsWrapperImpl();

    @Transactional
    public ItemVerificadoResponse verificar(String codigoProduto) {
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

        for (EstoqueItem estoqueItem : itens) {
            Optional<Item> itemExistente = itemRepository.findByCodBarrasAndCentroDistribuicao(estoqueItem.getCodBarras(),centroDistribuicao);
            Item itemAtualizado = itemExistente.get();
            itemAtualizado.setQuantidade(itemAtualizado.getQuantidade() + estoqueItem.getQuantidade());
            itemRepository.save(itemAtualizado);
            response.add(ItemMapper.toResponse(itemAtualizado));
        }

        return response;
    }

    public List<ItemResponse> listarItensNaoValidados() {
        return itemRepository.findByValidado(false).stream()
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

    public EditarItemResponse aprovarItem(UUID idItem, EditarItemRequest request) {
        Usuario solicitante = buscarUsuarioLogadoService.getLogado();
        Item editado = itemRepository.findByIdAndCentroDistribuicao(idItem, solicitante.getCentroDistribuicao())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, ITEM_NAO_ENCONTRADO));

        editado.setNome(isBlank(request.getNome()) ? editado.getNome() : request.getNome());
        editado.setCategoria(isBlank(request.getCategoria()) ? editado.getCategoria() : request.getCategoria());
        editado.setDescricao(isBlank(request.getDescricao()) ? editado.getDescricao() : request.getDescricao());
        editado.setValorMedida(request.getValorMedida());
        editado.setUnidadeMedida(isBlank(request.getUnidadeMedida()) ? editado.getUnidadeMedida() : request.getUnidadeMedida());
        editado.setValidado(true);

        itemRepository.save(editado);
        return EditarItemResponse.builder()
                .status(EDICAO_ADMIN_OK)
                .build();
    }
}
