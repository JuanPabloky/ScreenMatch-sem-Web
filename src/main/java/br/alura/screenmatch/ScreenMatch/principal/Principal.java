package br.alura.screenmatch.ScreenMatch.principal;

import br.alura.screenmatch.ScreenMatch.model.DadoSerie;
import br.alura.screenmatch.ScreenMatch.model.DadosEpisodios;
import br.alura.screenmatch.ScreenMatch.model.DadosTemporada;
import br.alura.screenmatch.ScreenMatch.model.Episodios;
import br.alura.screenmatch.ScreenMatch.service.ConsumoAPI;
import br.alura.screenmatch.ScreenMatch.service.ConverteDado;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;



public class Principal {
    Scanner ler = new Scanner(System.in);

    private ConsumoAPI consumo = new ConsumoAPI();

    private ConverteDado conversor = new ConverteDado();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=c5171690";

    public void exibeMenu(){
        System.out.println("Digite o nome da serie");
        var nomeSerie = ler.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadoSerie dados = conversor.obterDados(json,DadoSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i <=dados.totalTemporadas() ; i++) {
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+")+ "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);


        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodios> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 episodios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("primeiro filtro(N/A) " + e))
                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
                .peek(e -> System.out.println("Ordenação " + e))
                .limit(10)
                .peek(e -> System.out.println("Limite " + e))
                .map(e -> e.titulo().toUpperCase())
                .peek(e -> System.out.println("Mapeamento " + e))
                .forEach(System.out::println);

        List<Episodios> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodios(t.numero(), d))
                ).collect(Collectors.toList());
        episodios.forEach(System.out::println);


        //ENCONTRAR EPISODIO

//        System.out.println("Digite um trecho do titulo do episodio");
//        var trechoTitulo = ler.next();
//        Optional<Episodios> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//
//        if (episodioBuscado .isPresent()){
//            System.out.println("Episodio encontrado!");
//            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
//        }else {
//            System.out.println("Episodio não encontrado!");
//        }


//
//        System.out.println("Apartir de qual ano deseja analisar os episodios ?");
//        var ano = ler.nextInt();
//        ler.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1 ,1);
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getAnolancado() != null && e.getAnolancado().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                "Episodio: " + e.getTitulo() +
//                                "Data de Lançamento: " + e.getAnolancado().format(formatador)
//                ));


        Map<Integer, Double> avaliacaoPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() >0.0)
                .collect(Collectors.groupingBy(Episodios::getTemporada,
                        Collectors.averagingDouble(Episodios::getAvaliacao)));
        System.out.println(avaliacaoPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao()> 0.0)
                .collect(Collectors.summarizingDouble(Episodios::getAvaliacao));
        System.out.println(est);


    }
}
