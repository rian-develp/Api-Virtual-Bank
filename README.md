💳 API de Gerenciamento de Clientes e Cartões

API REST desenvolvida com Spring Boot para gerenciamento de clientes e seus cartões de crédito.

O sistema permite cadastrar clientes, associar cartões de crédito e removê-los quando necessário.

🚀 Funcionalidades

✅ Cadastrar cliente

✅ Fazer Login de cliente

✅ Adicionar cartão de crédito a um cliente

✅ Remover cartão de crédito


🛠️ Tecnologias Utilizadas

Java 17+

Spring Boot

Spring Data JPA

Hibernate

Banco de Dados Relacional

Maven

🗄️ Banco de Dados

A aplicação utiliza banco de dados relacional com mapeamento via JPA/Hibernate.

Relacionamento implementado:

Cliente → pode possuir múltiplos Cartões

Relacionamento: @OneToMany / @ManyToOne

▶️ Como Executar o Projeto
<br>
<br>
1️⃣ Clonar o repositório
git clone https://github.com/seu-usuario/seu-repositorio.git

2️⃣ Acessar a pasta do projeto
cd nome-do-projeto

3️⃣ Executar a aplicação

Se estiver usando Maven:

./mvnw spring-boot:run


Ou:

mvn spring-boot:run


A aplicação estará disponível em: http://localhost:8080


⚠️ Atualmente o projeto não possui autenticação/autorização, mas está planejada a implementação de:

🔒 Spring Security

🔑 Autenticação com JWT

👤 Controle de acesso por usuário

📌 Melhorias Futuras

Implementação de Spring Security

Documentação automática com Swagger/OpenAPI

Testes automatizados (JUnit e Mockito)
