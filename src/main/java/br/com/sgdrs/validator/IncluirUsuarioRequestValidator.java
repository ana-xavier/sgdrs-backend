//package br.com.sgdrs.validator;
//
//import br.com.sgdrs.controller.request.IncluirUsuarioRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ResponseStatusException;
//
//import static org.springframework.http.HttpStatus.BAD_REQUEST;
//
//@Component
//public class IncluirUsuarioRequestValidator {
//    private static final int TAMANHO_MAXIMO_NOME = 250;
//    private static final int TAMANHO_MAXIMO_EMAIL = 250;
//    private static final int TAMANHO_MAXIMO_SENHA = 250;
//    private static final String MENSAGEM_PADRAO_TAMANHO = "O campo %s tem tamanho máximo igual a %d";
//    private static final String MENSAGEM_PADRAO_NULL = "O campo %s não pode ser vazio/nulo";
//    /**
//     * Validação de dados de IncluirUsuarioRequest posteriormente
//     * será feita aqui
//     *
//     * */
//    public static void validar(IncluirUsuarioRequest request){
//        if(request.getNome().length() > TAMANHO_MAXIMO_NOME){
//            String mensagem = String.format(MENSAGEM_PADRAO_TAMANHO, "nome", TAMANHO_MAXIMO_NOME);
//            throw new ResponseStatusException(BAD_REQUEST, mensagem);
//        }
//
//        if(request.getEmail().length() > TAMANHO_MAXIMO_EMAIL){
//            String mensagem = String.format(MENSAGEM_PADRAO_TAMANHO, "email", TAMANHO_MAXIMO_EMAIL);
//            throw new ResponseStatusException(BAD_REQUEST, mensagem);
//        }
//
//        if(request.getSenha().length() > TAMANHO_MAXIMO_SENHA){
//            String mensagem = String.format(MENSAGEM_PADRAO_TAMANHO, "senha", TAMANHO_MAXIMO_SENHA);
//            throw new ResponseStatusException(BAD_REQUEST, mensagem);
//        }
//    }
//}
