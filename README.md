# 🔍 Eureka Service

O **Eureka Service** é um serviço responsável pelo **registro e descoberta de serviços** dentro da arquitetura
de serviços da aplicação.

Ele utiliza o [Spring Cloud Netflix Eureka](https://spring.io/projects/spring-cloud-netflix) para permitir que os demais
serviços se registrem e se localizem dinamicamente, eliminando a necessidade de configuração manual de endereços.

---

## 🚀 Funcionalidades

* Atua como **servidor Eureka** (*Service Registry*);
* Mantém uma lista atualizada dos serviços disponíveis;
* Permite **balanceamento de carga** e **resiliência** entre instâncias;
* Facilita a **escalabilidade horizontal** dos serviços;
* Integra-se com o **Spring Cloud Gateway** e outros clientes Eureka.

---

## 🧠 Como Funciona

1. O **Eureka Service** inicia como um **servidor Eureka**;
2. Outros serviços (ex: `api-gateway`) são configurados como **clientes Eureka**;
3. Cada cliente se registra automaticamente no servidor;
4. Ao se comunicar com outro serviço, a aplicação consulta o **Eureka Service**, que retorna a instância disponível.

---

## 🧩 Tecnologias Utilizadas

* [Java](https://www.oracle.com/java/) – Linguagem principal;
* [Spring Boot](https://spring.io/projects/spring-boot) – Framework para inicialização e gestão da aplicação;
* [Spring Cloud Netflix Eureka Server](https://spring.io/projects/spring-cloud-netflix) – Registro e descoberta de serviços;
* [Gradle](https://gradle.org/) – Sistema de build;
* [Maven](https://maven.apache.org/) – Sistema de build;
* [Docker](https://www.docker.com/) – Empacotamento e deploy containerizado.

---

## ⚙️ Variáveis de Ambiente

A aplicação utiliza variáveis de ambiente para configuração, com suporte a valores padrões definidos no `application.yml`.

Formato utilizado:

```
${NOME_VARIAVEL:valor_padrao}
```

### 📋 Configurações disponíveis

| Variável              | Descrição                                         | Default                         |
|-----------------------|---------------------------------------------------|---------------------------------|
| `EUREKA_HOSTNAME`     | Hostname da instância do serviço Eureka           | `localhost`                     |
| `EUREKA_URL`          | URL do servidor Eureka para registro e descoberta | `http://localhost:8761/eureka/` |
| `HEALTH_SHOW_DETAILS` | Controla a exibição de detalhes do health check   | `always`                        |

> Caso a variável não seja definida, o valor padrão especificado será utilizado automaticamente pela aplicação.

---

## 🛠️ Build Local

### 📦 Pré-requisitos

Antes de realizar o Build da aplicação, certifique-se de que as seguintes dependências estão instaladas e configuradas
em seu ambiente:

* [Java 25](https://www.oracle.com/java/)
* [Gradle 9.4.0 +](https://gradle.org/) *(ou utilize o wrapper incluído no projeto)*
* [Maven](https://maven.apache.org/)

### 🚀 Executando o Build

No diretório raiz do projeto, execute um dos comandos abaixo:

```bash
gradle clean build
```

Ou utilizando o wrapper do Gradle (**recomendado**):

```bash
./gradlew clean build
```

### 📁 Artefato Gerado

Após a execução do build, o arquivo `.jar` será gerado no seguinte diretório:

```
/build/libs/eureka.jar
```

---

## 🐳 Build com Docker

Para gerar a imagem Docker da aplicação, utilize a task customizada do Gradle:

``` bash
./gradlew clean docker-build -Penv=dev
```

### ⚙️ Parâmetros

Parâmetro obrigatório que define o ambiente da imagem.

### 🔹 -Penv
**Valores permitidos:** [ dev, test, homolog, prod ]

**Exemplo:**

``` bash
./gradlew clean docker -Penv=test
```

Ao executar a task `docker`, o Gradle realiza automaticamente:
-   Executa a task `bootJar`
-   Gera o `.jar` da aplicação em:
```
    /build/libs/eureka.jar
```

### 🐳 Build da imagem Docker

-   Executa o `docker build`
-   Utiliza o `Dockerfile` do projeto

### 🏷️ Geração de tags da imagem

-   Sempre gera a tag com a versão:

        eureka:${VERSION}

-   Gera também a tag do ambiente informado:

        eureka:${ENV}

## 🏷️ Exemplos de saída

### 🔹 Exemplo com dev

``` bash
./gradlew clean docker-build -Penv=dev
```

**Gera as imagens:** - eureka:1.0.0 - eureka:dev

### 🔹 Exemplo com test

``` bash
./gradlew clean docker-build -Penv=test
```

**Gera:** - eureka:1.0.0 - eureka:test

## ❗ Validações aplicadas

A task possui validações para garantir consistência:

-   Caso o parâmetro `env` não seja informado, o Build será interrompido
-   Caso seja informado um valor inválido, o Build será interrompido

Isso evita erros em pipelines e padroniza os ambientes.

## 📦 Dockerfile

A imagem é construída a partir do Dockerfile do projeto, que:

-   Utiliza uma imagem base leve (`eclipse-temurin:25-jre-alpine`)
-   Copia o `.jar` gerado pelo Gradle
-   Configura timezone (`America/Sao_Paulo`)
-   Expõe a porta `8761`
-   Define um `HEALTHCHECK` para o endpoint `/actuator/health`

## 🔄 Integração com CI/CD

Essa task foi projetada para ser utilizada em pipelines.

**Exemplo:**

``` bash
./gradlew clean docker-build -Penv=test
```

Após isso, a pipeline pode:

-   Realizar login no registry
-   Fazer push das imagens
-   Executar o deploy no ambiente correspondente

## 💡 Boas práticas

-   Utilize `clean` em pipelines para garantir builds consistentes
-   Utilize `-Penv` para padronizar ambientes (`dev`, `test`, `homolog`, `prod`)
-   Não inclua etapas de deploy dentro do Gradle (responsabilidade do pipeline)

## 🔄 Executar no docker como Dev

``` bash
docker run -d --name eureka --restart always -p 8761:8761 -e EUREKA_URL=http://eureka:8761/eureka/ -e HOSTNAME=eureka --network houpper-network eureka:dev
```
> Para a execução ocorrer corretamente a rede docker 'houpper-network' deve existir.

---

## 📤 Build e Push no GHCR (Em construção)

Para automatizar build, tag e push da imagem para o GitHub Container Registry, use:

``` bash
scripts/build-and-push-image.sh <env>
```

### 📦 Pré-requisitos

- Docker instalado e disponível no `PATH`
- Permissão para publicar pacote no GHCR
- Variáveis obrigatórias:
    - `GITHUB_USERNAME`
    - `GITHUB_TOKEN`
    - `GH_OWNER`

### ⚙️ Ambientes permitidos

`dev`, `test`, `homolog`, `latest`

### ✅ Exemplo completo

``` bash
GITHUB_USERNAME=seu_usuario \
GITHUB_TOKEN=seu_token \
GH_OWNER=sua_org \
scripts/build-and-push-image.sh dev
```

### 🏷️ Tags publicadas

O script publica duas tags:

- `ghcr.io/<gh_owner>/discovery-service:<version>`
- `ghcr.io/<gh_owner>/discovery-service:<env>`

---