package br.com.sgdrs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import pl.coderion.model.Product;
import pl.coderion.model.ProductResponse;
import pl.coderion.service.OpenFoodFactsWrapper;
import pl.coderion.service.impl.OpenFoodFactsWrapperImpl;

@Service
public class EstoqueService {
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

        return ItemVerificadoMapper.toResponse(null, false);
    }
    @Transactional
    public  List<ItemResponse> cadastrarItens(EstoqueRequest request){
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
}
