package br.alura.screenmatch.ScreenMatch;

import br.alura.screenmatch.ScreenMatch.model.DadoSerie;
import br.alura.screenmatch.ScreenMatch.service.ConsumoAPI;
import br.alura.screenmatch.ScreenMatch.service.ConverteDado;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=c5171690");
		System.out.println(json);
		ConverteDado conversor = new ConverteDado();
		DadoSerie dados = conversor.obterDados(json,DadoSerie.class);
		System.out.println(dados);
	}
}
