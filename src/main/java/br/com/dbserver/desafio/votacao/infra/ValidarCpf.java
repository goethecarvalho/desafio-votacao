package br.com.dbserver.desafio.votacao.infra;

import br.com.caelum.stella.validation.CPFValidator;

public class ValidarCpf {

    public static boolean valida(String cpf) {
        CPFValidator cpfValidator = new CPFValidator();
        try{ cpfValidator.assertValid(cpf);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
