package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Date;

import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.rules.ErrorCollector;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	private LocacaoService service;
	
	private static int contador = 0;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Before
	public void setup() {
		System.out.println("Before");
		service = new LocacaoService();
		
		contador++;
		System.out.println(contador);
	}
	
	@After
	public void tearDown() {
		System.out.println("After");
	}
	
	@BeforeClass
	public static void setupClass() {
		System.out.println("Before Class");
	}
	
	@AfterClass
	public static void tearDownClass() {
		System.out.println("After Class");
	}
	
	@Test
	public void testeLocacao() throws Exception{
		
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme", 2, 5.0);
		
		System.out.println("teste!");
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filme);
			
		//verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()),is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));

	}
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void testLocacao_filmeSemEstoque_1() throws Exception {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);
		
		//acao
		service.alugarFilme(usuario, filme);		
	}
	
	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {
		//cenario
		Filme filme = new Filme("Filme 1", 1, 5.0);
		
		//acao
		try {
			service.alugarFilme(null, filme);
		} catch (LocadoraException e) {
			MatcherAssert.assertThat(e.getMessage(), is("Usuario vazio"));;
		}

	}
	
	@Test
	public void testLocacao_FilmeVazio() throws FilmeSemEstoqueException {
		//cenario
		final Usuario usuario = new Usuario("Usuario 1");
		
		//acao
		ThrowingRunnable throwingRunnable = new ThrowingRunnable() {
			public void run() throws Throwable {
				service.alugarFilme(usuario, null);
			}
		};
		
		Assert.assertThrows("Filme vazio", LocadoraException.class, throwingRunnable);
		
	}
}
