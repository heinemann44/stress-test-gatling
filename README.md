# Testar aplicação backend

Teste feito com a ferramenta gatling, para rodar o script de teste é necessário primeiro instalar o gatling

```sh
bash install-gatling-sh
```

Caso queira baixar manualmente e/ou alterar a pasta em que está salvo o gatling precisa ir no arquivo `run-test-sh` e ajustar a variável `GATLING_BIN_DIR`

Os testes são feitos nos endpoints:

```sh
curl -v -XPOST -H "content-type: application/json" -d '{"apelido" : "xpto", "nome" : "xpto xpto", "nascimento" : "2000-01-01", "stack": ["Java"]}' "http://localhost:9999/pessoas"
curl -v -XGET "http://localhost:9999/pessoas/1"
curl -v -XGET "http://localhost:9999/pessoas?t=xpto"
```

## Gerando carga de teste com python

```sh
python3 geracao_recursos.py
```

## Rodar testes

```sh
bash run-test-sh
```
