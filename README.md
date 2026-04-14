# Sistema CustomPC E-Commerce

## Descrição

Este sistema gerencia uma plataforma de comércio eletrônico especializada na venda de componentes de hardware e montagem de computadores personalizados. O grande diferencial é o motor de validação de compatibilidade, que garante que as peças escolhidas pelo cliente (Processador, Placa-mãe, Fonte) funcionem corretamente juntas, evitando devoluções por erros técnicos.

Além da venda de peças avulsas, o sistema permite que o usuário salve "Builds" (configurações) e receba sugestões baseadas no consumo energético total. O fluxo logístico acompanha desde a separação no estoque até a montagem técnica e envio final.

## Requisitos Funcionais

### 1. Gestão de Catálogo Técnico
- **REQ01**: Cadastrar componentes com especificações (Socket, TDP, RAM Type).
- **REQ02**: Utilizar herança para organizar categorias (Processador, Placa-Mãe, etc).
- **REQ03**: Controlar o estoque físico unitário de cada componente.

### 2. Motor de Compatibilidade
- **REQ04**: Validar se o Socket do processador é idêntico ao da placa-mãe.
- **REQ05**: Verificar se o tipo de memória RAM é suportado pela placa-mãe escolhida.
- **REQ06**: Somar o consumo total (Watts) e validar se a fonte suporta a carga.

### 3. Carrinho e Pedidos
- **REQ07**: Permitir a criação de configurações personalizadas ("Builds").
- **REQ08**: Implementar Carrinho de Compras (composição) para peças e serviços.
- **REQ09**: Registrar pedidos com status: Pagamento, Montagem, Teste, Enviado.

### 4. Logística e Financeiro
- **REQ10**: Calcular frete com base no peso e volume da embalagem final.
- **REQ11**: Aplicar descontos progressivos para PCs completos.
- **REQ12**: Gerar Nota Fiscal eletrônica em PDF.

### 5. Relatórios
- **REQ13**: Gerar relatório de itens com baixo estoque.
- **REQ14**: Exportar faturamento mensal detalhado por margem de lucro.
- **REQ15**: Listar ordens de montagem pendentes para a equipe técnica.

### 6. Regras e Restrições
- **REQ16**: **Bloquear a finalização** de um PC montado caso o motor detecte incompatibilidade grave (ex: Socket diferente).
- **REQ17**: **Não permitir** a inclusão de produtos com estoque zerado no carrinho.
- **REQ18**: **Impedir o cancelamento** de pedidos que já estejam no status de "Enviado".
- **REQ19**: **Validar** se as dimensões da placa de vídeo cabem no gabinete selecionado.
- **REQ20**: **Bloquear cupons** de desconto caso o valor mínimo da compra não seja atingido.
- **REQ21**: **Garantir** que pedidos de montagem incluam obrigatoriamente Processador, Placa-mãe e RAM.

## Possíveis APIs/Bibliotecas
- **Jackson/Gson** – Manipulação de JSON.
- **Apache POI** – Relatórios em Excel.
- **iText** – Geração de notas fiscais.


## Integrantes do grupo
- Brenna Vitória de Souza Marinho: brenna.marinho@ufrpe.br
- ⁠Maria Clara Freitas de Barros Correia: clarafreitas06cf@gmail.com
- Lucas Carvalho Oliveira da Silva: lucascarvalhoo@outlook.com
- João Augusto Ferreira Andrade Leitão: joao.aferreira@ufrpe.br
- Sameque kadmiel Dias Sousa: Sameque.kadmiel@gmail.com
