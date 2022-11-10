package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Date;

import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.rules.ErrorCollector;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Test
	public void testeLocacao() throws Exception{
		
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme", 2, 5.0);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filme);
			
		//verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()),is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));

	}
	
	@Test(expected=Exception.class)
	public void testLocacao_filmeSemEstoque_1() throws Exception {
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);
		
		//acao
		service.alugarFilme(usuario, filme);		
	}
	
	@Test
	public void testLocacao_filmeSemEstoque_2() {
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);
		
		//acao
		try {
			service.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lancado uma excecao");
		} catch (Exception e) {
		MatcherAssert.assertThat(e.getMessage(), is("filme sem estoque"));
		}		
	}
	
	@Test
	public void testLocacao_filmeSemEstoque_3() throws Exception {
	   //cenario
	   final LocacaoService service = new LocacaoService();
	   final Usuario usuario = new Usuario("Usuario 1");
	   final Filme filme = new Filme("Filme 2", 0, 4.0);
	 
	   //acao
	   ThrowingRunnable throwingRunnable = new ThrowingRunnable() {
	      
	      public void run() throws Throwable {
	         service.alugarFilme(usuario, filme);
	      }
	   };
	 
	   Assert.assertThrows("Filme sem estoque", Exception.class, throwingRunnable);
	}
}
