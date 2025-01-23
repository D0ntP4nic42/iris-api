# Sistema Íris
Sistema **Íris** de gerenciamento acadêmico.

## Objetivo do sistema
O sistema tem como objetivo auxiliar e substituir parte do trabalho manual realizado pelos servidores do **CEDLAN**, facilitando o gerenciamento de notas e de turmas das disciplinas eletivas (antes feito por uma tabela no Excel). O sistema será desenvolvido por 3 grupos divididos entre Frontend, Backend e o banco de dados/servidor. Esse repositório contém o Backend.

## Tecnologias Frontend:
- **Svelte**
- **Tailwind**
- **DaisyUI**
- **Git**

## Tecnologias Backend:
- **Java 21**
- **SpringBoot 3.4.1**
- **Maven 4.0.0**

## Equipe Frontend:
**Luiz Gustavo**: Programação geral do front end, UI/UX e gerenciamento de requisitos (Github: https://github.com/1917dc) \
**Karoline Rodrigues**: Desenvolvimento de todo Front-end relacionado ao acesso de administradores ao sistema (Github: ) \
**Vinícius Rodrigues**: Desenvolvimento de todo Front-end relacionado ao acesso de professores ao sistema (Github: ) \
**Raquel Pereira**: Desenvolvimento de todo Front-end relacionado ao acesso de alunos ao sistema (Github: ) \

## Equipe Backend:
**Leonardo Brito**: Desenvolvimento (Github: https://github.com/D0ntP4nic42)  
**Breno Amorim**: Desenvolvimento (Github: ) 

## Como rodar nosso projeto
Para rodar nosso projeto basta clonar o respositório com:
```bash
git clone "https://github.com/nomeDoPerfil/nomeDoRepositório.git"
```
O próximo passo é usar o comando **cd** para navegar até pasta **iris-app**:
```bash
cd ../iris/iris-app
```
Com seu console dentro da pasta **iris-app** use:
```bash
npm install
```
Seguido de:
```bash
npm run dev
```

Agora utilizando sua IDE Java de preferência (Eclipse, Intellij, VsCode), rode o backend presente no diretório **iris-api**.  
  
Para rodar em Docker é necessário apenas rodar o comando:
```bash
docker-compose up --build -d
```
O comando pode ser utilizado nas duas pastas (**iris-app** e **iris-api**).

O projeto agora está rodando, basta abrir o navegador no localhost com a porta que aparece no terminal.

## Detalhes do desenvolvimento (Backend):
Nosso projeto utiliza Spring Security com OAuth2 (chaves RSA) como forma de segurança.  
  
Ao iniciar o projeto é feita a inserção de um professor que tem suas informações fornecidas ao programa através do **application.properties**.  

Boa parte do desenvolvimento da segurança foi feito com base no vídeo: https://www.youtube.com/watch?v=nDst-CRKt_k

### Planos futuros:
* Avaliar a viabilidade de continuar usando o formato atual de formatação do banco (De forma mais específica, as tabelas de User, Professor e Aluno).  
* Mudar as chaves públicas e privadas antes do deploy (Além de achar alguma forma de fornecer elas quando necessário sem precisar guardar no git) da aplicação por questões de segurança.  
* Avaliar a possibilidade ~~de tratar coordenadores como um boolean (Visto que eles são professores) de forma que~~ sempre que algum coordenador quiser tornar outro professor coordenador o seu boolean se tornará falso enquanto o do outro se tornará verdadeiro (Assim manetendo apenas um coordenador)  

    * Descartada a possibilidade de utilizar boolean pois enum de roles ficou mais simples e mais padronizado com o passar do desenvolvimento, o resto da lógica continua sendo utilizada.

* Possivelmente fazer com que o sistema gere uma senha aleatório para cada usuário durante a criação e envie um email contendo as informações de login (Incluindo a senha).
* Implementar o docker-compose.
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