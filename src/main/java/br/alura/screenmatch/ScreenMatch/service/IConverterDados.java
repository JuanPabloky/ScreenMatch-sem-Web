package br.alura.screenmatch.ScreenMatch.service;

public interface IConverterDados {
    <T> T obterDados(String json, Class <T> classe);
}
