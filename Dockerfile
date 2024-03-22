# Usar uma imagem base oficial do Maven para compilar o projeto
FROM maven:3.8.4-openjdk-17 as build

# Definir o diretório de trabalho no container
WORKDIR /app

# Primeiro copie o arquivo pom.xml separadamente para aproveitar o cache das dependências
COPY pom.xml .

# Opcional: Se seu projeto tem módulos ou dependências locais, você pode precisar copiá-los antes de rodar o mvn package
# COPY src /app/src
# COPY lib /app/lib (Se você tiver diretórios de bibliotecas personalizadas, etc.)

# Agora copie o restante dos arquivos do projeto
COPY . .

# Compilar o projeto e empacotá-lo em um arquivo .jar
RUN mvn clean package

# Usar a imagem base do OpenJDK para rodar a aplicação
FROM openjdk:17

# Copiar o arquivo .jar do estágio de compilação para o diretório /app da nova imagem
COPY --from=build /app/target/*.jar /app/app.jar

# Definir o diretório de trabalho
WORKDIR /app

# Comando para executar a aplicação
CMD ["java", "-jar", "app.jar"]
