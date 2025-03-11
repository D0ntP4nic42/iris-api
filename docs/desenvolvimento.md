## Sobre o desenvolvimento (Backend)
### Introdução:
Esse documento tem como objetivo descrever um pouco da jornada de desenvolvimento do backend mostrando a razão por trás de algumas escolhas e de abandono de algumas ideias.

Nosso projeto utiliza Spring Security com OAuth2 (chaves RSA) como forma de segurança.  
  
Ao iniciar o projeto é feita a inserção de um professor que tem suas informações fornecidas ao programa através do **application.properties** (Admin padrão: cpf = 882.354.885-31, senha = 1234).  

Boa parte do desenvolvimento da segurança foi feito com base no vídeo: https://www.youtube.com/watch?v=nDst-CRKt_k

### Planos futuros:
* Avaliar a viabilidade de continuar usando o formato atual de formatação do banco (De forma mais específica, as tabelas de User, Professor e Aluno).  
* Mudar as chaves públicas e privadas antes do deploy (Além de achar alguma forma de fornecer elas quando necessário sem precisar guardar no git) da aplicação por questões de segurança.  
* Avaliar a possibilidade ~~de tratar coordenadores como um boolean (Visto que eles são professores) de forma que~~ sempre que algum coordenador quiser tornar outro professor coordenador o seu boolean se tornará falso enquanto o do outro se tornará verdadeiro (Assim manetendo apenas um coordenador)  

    * Descartada a possibilidade de utilizar boolean pois enum de roles ficou mais simples e mais padronizado com o passar do desenvolvimento, o resto da lógica continua sendo utilizada.

* ~~Possivelmente fazer com que o sistema gere uma senha aleatória para cada usuário durante a criação e envie um email contendo as informações de login (Incluindo a senha).~~
    * Em uma das reuniões com o coordenador do CEDLAN foi sugerido o uso de uma senha padrão para todos os alunos inicialmente, com a possibilidade de troca depois. Também fomos informados que era melhor não utilizar e-mail na aplicação.
* ~~Implementar o docker-compose.~~
    * Implementado
* Pensar em uma forma de tratar os horários das aulas  
    * Sugestão 1: Uma tabela **horario** no banco de dados que armazena um objeto que contém o dia da semana, o horário de fim e o de começo, além de uma chave de referência a respectiva turma.

### Problemas conhecidos:
- Possível falha no scan de componentes realizado pelo Spring. Apesar de todos estarem em pacotes no mesmo nível do inicializador, ele não consegue achar as classes do pacote de repositories (Solução temporária foi adicionar uma annotation no inicializador).  
- "Falha" na incialização pois o java solta uma Exception dizendo que o banco já possui a tabela que eles está tentando criar ou não possui a tabela que ele está tentando referenciar. O progama roda normalmente mesmo com essa Exception.

### Documentação em geral:
A ideia desta seção é exibir possívies explicações para classes/métodos/annotations que foram utilizados e podem ser incomuns ao conhecimento geral dos desenvolvedores.

#### DataLoader.java:
Classe que realiza uma acção sempre que a aplicação começa a rodar. No caso está sendo usada para adicionar um professor administrador caso não haja um no banco.

#### DatasourceProperties.java:
Classe que armazena configurações do banco, sintáxe um pouco complicada e pode necessitar alguma leitura mas em geral ela é uma classe que após ser criada pode ser ignorada.

#### OpenApiConfiguration.java:
Uma classe para configuração do Swagger, em geral, pode ser ignorada.

#### @SecurityRequirement(name = "bearerAuth"):  
Uma annotation que deve ser utilizada em todo endpoint que requer segurança para informar o Swagger que ele deve passar o token no header da requisição. Caso todos os endpoints de um controller precisem de autenticação, a annotation pode ser utilizada no próprio controller para evitar repetição.

#### Roles enum:
Um enum de roles presente no pacote de security é basicamente uma lista de roles com descrições para padronizar o uso por todo o código. Utilizando *"Role.COORDENADOR.name()"* por exemplo, você coloca a role do usúario como coordenador.

### Comandos/argumentos úteis:
Alguns comandos diversos que podem ser úteis

#### Comandos de inicialização do docker:
```
docker-compose -f docker-compose-local.yml up --build -d mysql
```
Existem três argumentos nesse comando que podem ser úteis por diversas razões.

* -f docker-compose-local.yml:
    * Caso haja mais de um ambiente (exemplo: produção e desenvolvimento) esse comando ajuda a separar os composes de cada ambiente.
* -d:
    * Um comando simples para dar "detach" do container no console que você executou o comando, "desvinculando" o container do console e liberando ele assim que subir.
* mysql:
    * Possivelmente o mais útil dos 3. Serve para indicar ao docker que você quer subir apenas **UM** serviço presente no respectivo compose (Caso o compose já tenha sido executado anteriormente e esteja em pé, apenas o serviço escolhido será reiniciado, os outros permanecerão iguais).

O resultado final do comando em questão em nosso projeto seria apenas um container com o nosso banco.

### Sonarqube:
O Sonarqube é uma aplicação para análise de código, ela detecta problemas de segurança, confiabilidade e manutenibilidade.

![Screenshot do Sonarqube](/docs/assets/screenshot_sonarqube.png?raw=true)

Para executar a varredura do sonarqube é necessário subir uma instância do próprio e adicionar o plugin e as variáveis no pom.

![Screenshot localização do token](/docs/assets/properties_pom.png?raw=true)
![Screenshot plugin SonarQube](/docs/assets/plugins_pom.png?raw=true)

O Plugin já está adicionado basta apenas subir uma instância do SonarQube (Utilize isso como base: https://www.youtube.com/watch?v=sQ7_pcaw-iM). No vídeo ele gera um token em uma versão desatualizada do Sonar. Na mais atual vai ter uma opção para escolher o tipo de token, selecione "User".  

![Screenshot plugin SonarQube](/docs/assets/screenshot_token.png?raw=true)

Após realizar toda a configuração inicial do Sonar o comando  ``` clean verify sonar:sonar``` executa o scanner (Lembre-se de adicionar o token no pom).