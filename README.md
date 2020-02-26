# Leucotron App
Desafio proposto pela Empresa Leucotron, localizada em Santa Rita do Sapucaí.

## Descrição
O aplicativo tem como finalidade o armazenamento de informações curriculares de canditados à vagas de emprego de uma determinada empresa.
Densenvolvido na plataforma Android Studio, através da linguagem de programação Java juntamente com o banco de dados Firebase para salvar os dados.

## Funcionalidades
O aplicativo contém algumas funcionalidades essenciais, como: 
- Cadastro do candidato;
- Editar as informaçãoes;
- Listar os dados do Banco de dados;
- Excluir;
- Pesquisar.

Além de duas funcionalidade extras, como:
- Pesquisa com filtro;
- Gerar um relatório em PDF(não implementado);
- Autenticação com Google;
- Telefonar para algum candidato.

## Bibliotecas utilizadas
#### Material design.

- Utilizado no design dos componentes do aplicativo (https://material.io/).

#### Firebase Authentication.

- Login com o email do google.

#### Firebase database.

- Armazenamento de dados

#### Firebase storage

- Armazenamento das fotos

#### Mask

- Máscara utilizada nos campos de telefone (https://github.com/rtoshiro/MaskFormatter)

#### Firebase database UI

- Utilizada para pesquisas e recuperações de dados (https://github.com/firebase/FirebaseUI-Android)

#### Glide

- Carregar uma imagem a partir de um URL recuperado (https://github.com/bumptech/glide)

## Wireframe 
![](https://github.com/jpgSouza/leucotron-app/blob/master/wireframe-app.jpeg)

## Como usar?
### Android Studio
- Importe o projeto para o Android Studio;
- Vá até Run -> Run App;
- Escolha o dispositivo ou crie um novo;
- Dispositivo utilizado foi o **Pixel 3 XL** de API level **28**.

Ao se deparar com a primeira *view* (Tela de login), serão necessárias duas informações (login e senha), porém tais informações já são pré-definidas para a realização do login.
- ***Username: admin***
- ***Senha: admin***

Após logado, o usuário pode navegar através da *dash board* para acessar as diferentes funcionalidades.

## Observação
Talvez seja necessário ativar no dispositivo emulado ou físico a permissão para utilização do telefone para que a funcionalidade de ligar para o candidato funcione corretamente.

<img src="https://github.com/jpgSouza/leucotron-app/blob/master/permissions-app.png" height="470" width="250">

