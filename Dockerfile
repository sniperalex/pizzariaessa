# --- Estágio 1: O Ambiente de Construção (Build) ---
# Usamos uma imagem oficial do Maven que já tem o Java 21 para compilar nosso projeto.
# O "AS build" dá um nome a este estágio para que possamos nos referir a ele mais tarde.
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Define o diretório de trabalho dentro do container.
WORKDIR /app

# Copia os arquivos do Maven Wrapper primeiro (para aproveitar o cache do Docker)
COPY mvnw .
COPY .mvn .mvn

# Copia o arquivo de definição do projeto (pom.xml)
COPY pom.xml .

# Baixa todas as dependências do projeto.
# Isso é feito separadamente para que o Docker possa usar o cache se as dependências não mudarem.
RUN mvn dependency:go-offline

# Agora, copia todo o resto do código-fonte da sua aplicação.
COPY src ./src

# Executa o comando para empacotar a aplicação em um arquivo .jar.
# O -DskipTests pula a execução dos testes para um build mais rápido.
RUN mvn package -DskipTests


# --- Estágio 2: O Ambiente de Execução (Runtime) ---
# Usamos uma imagem base minúscula que contém apenas o Java Runtime Environment (JRE).
# Isso torna nossa imagem final muito menor e mais segura.
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho.
WORKDIR /app

# Copia APENAS o arquivo .jar que foi gerado no estágio de "build" para o nosso container final.
# O nome do arquivo é padronizado para "app.jar".
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080, que é a porta padrão que o seu Spring Boot usa.
# O Render vai detectar isso e direcionar o tráfego para cá.
EXPOSE 8080

# O comando que será executado quando o container iniciar.
# Simplesmente executa a nossa aplicação.
ENTRYPOINT ["java", "-jar", "app.jar"]