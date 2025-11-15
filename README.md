## 1. Visão Geral do Projeto

O SafeBite é um sistema web construído em Java com o framework Spring Boot. Seu objetivo é auxiliar usuários no controle de alergias e intolerâncias alimentares.

A aplicação permite que os usuários se registem, façam login e acesse um painel pessoal onde podem gerir quatro funcionalidades centrais:
* **Carteirinha de Emergência:** Manter informações críticas para situações de emergência.
* **Histórico de Reações:** Registar detalhes sobre reações alérgicas sofridas.
* **Diário Alimentar:** Anotar alimentos consumidos para monitorização.
* **Gestão de Restrições:** Manter uma lista pessoal de restrições alimentares.

O sistema utiliza uma arquitetura MVC (Model-View-Controller) tradicional, com Spring Boot para o back-end, Thymeleaf para a renderização do front-end e Spring Data JPA para a persistência de dados.

## 2. Tecnologias Utilizadas

O projeto é baseado no ecossistema Spring e utiliza as seguintes tecnologias principais:

* **Linguagem:** Java 17
* **Framework Principal:** Spring Boot 3.5.7
* **Back-end:**
    * `Spring Web`: Para a criação de controladores web (MVC e REST).
    * `Spring Security`: Para autenticação, autorização e gestão de sessões.
    * `Spring Data JPA`: Para a camada de persistência e comunicação com o banco de dados.
    * `Validation`: Para validação de DTOs (Data Transfer Objects).
* **Banco de Dados:**
    * `PostgreSQL`: Configurado como o banco de dados principal.
    * `Hibernate`: Utilizado como a implementação JPA (via `spring-boot-starter-data-jpa`).
* **Front-end:**
    * `Thymeleaf`: Motor de templates Java para renderizar as páginas HTML do lado do servidor.
    * `HTML/CSS/JavaScript (jQuery)`: Para a estrutura e interatividade das páginas (ex: no `myRestrictions.html`).
* **Utilitários:**
    * `Lombok`: Para reduzir a repetiçao do codigo (como getters, setters e construtores) nas classes de modelo e DTOs.
    * `Maven`: Para gestão de dependências e build do projeto.

## 3. Arquitetura do Sistema

O código está organizado numa arquitetura de camadas padrão do Spring Boot:

* **`com.safebite.model` (Entidades):** Classes Java (POJOs) anotadas com `@Entity` que representam as tabelas do banco de dados. Exemplos: `User`, `Reaction`, `FoodDiary`, `EmergencyCard`, `Restriction`.
* **`com.safebite.repository` (Repositórios):** Interfaces que estendem `JpaRepository`. O Spring Data JPA implementa automaticamente os métodos de acesso ao banco de dados (CRUD). Exemplos: `UserRepository`, `ReactionRepository`.
* **`com.safebite.service` (Serviços):** Classes que contêm a lógica de negócios. Elas coordenam os repositórios e realizam as operações. Exemplos: `UserService`, `ReactionService`.
* **`com.safebite.controller` (Controladores):**
    * `ViewController.java`: O controlador principal da aplicação web (MVC). Gere a navegação entre páginas e o processamento de formulários Thymeleaf.
    * Controladores REST (ex: `UserController`, `DiaryController`): Expõem endpoints de API (prefixo `/api`) que manipulam JSON, provavelmente destinados a um futuro cliente móvel ou SPA (Single Page Application).
* **`com.safebite.dto` (Data Transfer Objects):** Classes simples usadas para transferir dados entre os controladores e os serviços, e para validação de formulários. Exemplos: `UserRegisterDTO`, `ReactionDTO`.
* **`com.safebite.config` (Configuração):** Contém a configuração de segurança (`SecurityConfig.java`).

## 4. Estrutura do Banco de Dados (Schema)

O esquema do banco de dados é definido pelas entidades JPA e pelo ficheiro `DDL.sql`.

* **`usuarios` (User.java):** Tabela central. Armazena nome, e-mail (único) e senha criptografada.
* **`restricoes` (Restriction.java):** Tabela de consulta para tipos de restrições (ex: Glúten, Lactose). Dados iniciais são populados pelo `DML.sql`.
* **`carteirinha_emergencia` (EmergencyCard.java):**
    * Relação **One-to-One** com `usuarios`.
    * Utiliza a chave primária do usuário (`user_id`) como sua própria chave primária (`@MapsId`).
    * Armazena contatos, medicamentos e instruções médicas.
* **`reacoes` (Reaction.java):**
    * Relação **Many-to-One** com `usuarios`.
    * Cada registo está ligado a um `user_id`.
    * Armazena detalhes da reação (alimentos, sintomas, data, etc.).
* **`diario_alimentar` (FoodDiary.java):**
    * Relação **Many-to-One** com `usuarios`.
    * Cada registo está ligado a um `user_id`.
    * Armazena o alimento, observações e data.
* **`usuario_restricoes` (Join Table):**
    * Relação **Many-to-Many** entre `usuarios` e `restricoes`.
    * Permite que um usuário tenha múltiplas restrições e que uma restrição pertença a múltiplos usuários.

## 5. Fluxos e Funcionalidades Implementadas

### 5.1. Autenticação e Segurança (Spring Security)

* **Configuração:** O `SecurityConfig.java` define as regras.
* **Registo (`/register`):**
     *   O `ViewController` recebe o `UserRegisterDTO` do formulário `register.html`.
     *   Chama o `UserService.registerUser`.
     *   O serviço codifica a senha com `BCryptPasswordEncoder`, cria um `User` e, crucialmente, cria uma `EmergencyCard` vazia associada a esse usuário.
     *   Salva o usuário (e a carteirinha em cascata) no banco.
* **Login (`/login`):**
     *   Gerido pelo Spring Security, usando a página `login.html`.
     *   Utiliza o `UserDetailsServiceImpl` para procurar o usuário por e-mail no banco de dados.
     *   O Spring Security compara a senha fornecida com a senha `BCrypt` armazenada.
* **Autorização:**
    * Páginas `/login`, `/register` e `/css/**` são públicas.
    * Todas as outras rotas (ex: `/`, `/history`, `/foodDiary`) exigem autenticação (`anyRequest().authenticated()`).

### 5.2. Dashboard

* **Página:** `home.html`.
* **Lógica:** O `ViewController` obtém o e-mail do usuário autenticado (`Principal`), procura o `User` no banco (via `UserService`) e injeta o nome do usuário no modelo para saudação.
* **Funcionalidade:** Apresenta links de navegação para as funcionalidades principais.

### 5.3. Carteirinha de Emergência (`/emergencyCard`, `/emergencyCard/view`)

* **Páginas:** `emergencyCard.html` (formulário de edição), `emergencyCardDisplay.html` (visualização).
* **Fluxo de Atualização:**
     *   O usuário submete o formulário de `emergencyCard.html` (POST para `/emergencyCard`).
     *   O `ViewController` recebe o `EmergencyCardDTO`.
     *   Chama `emergencyCardService.updateCard`, que encontra a carteirinha existente pelo e-mail do usuário, atualiza os campos e salva as alterações.
     *   Redireciona para a página de visualização `/emergencyCard/view`.

### 5.4. Histórico de Reações (`/reactions/new`, `/history`)

* **Páginas:** `reactionForm.html` (formulário de registo), `history.html` (listagem).
* **Fluxo de Registo:**
     *   O usuário submete o formulário de `reactionForm.html` (POST para `/reactions/new`).
     *   O `ViewController` recebe o `ReactionDTO`.
     *   Chama `reactionService.recordReaction`, que associa o `User` autenticado e salva a nova entidade `Reaction`.
* **Fluxo de Listagem e Exclusão:**
     *   Ao acessar a `/history`, o `ViewController` chama `reactionService.getReactionsForUser` para obter a lista de reações.
     *   A página `history.html` exibe a lista.
     *   Cada reação tem um botão "Excluir" que envia um POST para `/history/delete/{id}`. O `ViewController` captura este pedido e chama `reactionService.deleteReaction`, que verifica se a reação pertence ao usuário antes de a apagar.

### 5.5. Diário Alimentar (`/foodDiary`)

* **Página:** `foodDiary.html` (contém o formulário de registo e a listagem).
* **Lógica:** Esta página combina os fluxos de registo e listagem.
* **Registo:** O formulário (POST para `/foodDiary`) é gerido pelo `ViewController`, que chama `foodDiaryService.saveDiaryEntry`.
* **Listagem e Exclusão:** O controlador também popula o modelo com a lista de entradas de `diaryService.getDiaryForUser`. A exclusão segue o mesmo padrão do Histórico de Reações, usando a rota `/foodDiary/delete/{id}` e o serviço `diaryService.deleteDiaryEntry`.

### 5.6. Minhas Restrições (`/myRestrictions`)

* **Página:** `myRestrictions.html`.
* **Lógica de Carregamento:** O `ViewController` obtém todas as restrições (`restrictionService.getAllRestrictions`) e as restrições atuais do usuário (`user.getRestrictions()`) para pré-selecionar as checkboxes.
* **Fluxo de Atualização:**
     *   O formulário (POST para `/myRestrictions`) envia uma lista de `restrictionIds` e um campo de texto opcional `outraRestricao`.
     *   O `ViewController` chama `userService.updateRestrictionsForUser` com estes parâmetros.
     *   O `UserService` atualiza a relação `ManyToMany` (`Set<Restriction>`) no objeto `User`.
     *   **Funcionalidade "Outra":** Se `outraRestricao` for preenchida, o `UserService` chama `restrictionService.findOrCreateByName`. Este serviço verifica se uma restrição com esse nome já existe; se não, cria uma nova e salva-a no banco, antes de a adicionar ao perfil do usuário.

## 6. Configuração e Inicialização

 *   **Configuração:** O ficheiro `application.properties` define a conexão com o banco de dados PostgreSQL `safebite_db` em `localhost:5432`.
 *   **Schema:** A propriedade `spring.jpa.hibernate.ddl-auto=validate` significa que o sistema **não cria** ou atualiza as tabelas automaticamente. O esquema do banco de dados deve ser criado manualmente executando o `DDL.sql`.
 *   **Dados Iniciais:** O `DML.sql` deve ser executado para popular a tabela `restricoes` com os valores padrão (Glúten, Lactose, etc.).
