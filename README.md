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
Uma documentação com os detalhes do desenvolvimento do backend também foi feita. O arquivo está disponível na pasta "docs" desse repositório. É interessante que todos os desenvolvedores, caso achem alguma classe ou método que não conhecem e precisem mexer, leiam a documentação.