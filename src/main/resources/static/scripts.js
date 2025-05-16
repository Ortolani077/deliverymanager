/**
 * Função para alternar a visibilidade das seções com animação de colapso
 * @param {string} sectionId - ID da seção a ser exibida
 */
function toggleSection(sectionId) {
    const sections = document.querySelectorAll('section');
    sections.forEach(section => {
        section.classList.add('hidden-section');
    });

    const sectionToShow = document.getElementById(sectionId);
    if (sectionToShow) {
        sectionToShow.classList.remove('hidden-section');
        const closeBtn = sectionToShow.querySelector('.close-section');
        if (closeBtn) {
            closeBtn.onclick = () => sectionToShow.classList.add('hidden-section');
        }
    } else {
        console.error(`Seção com ID '${sectionId}' não encontrada.`);
    }
}

/**
 * Função para realizar logout do usuário
 */
function logout() {
    console.log('Iniciando processo de logout...');
    fetch('/api/auth/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) throw new Error('Erro ao fazer logout.');
        return response.text();
    })
    .then(() => {
        console.log('Logout bem-sucedido.');
        document.cookie = 'token=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/';
        localStorage.removeItem('token');
        setTimeout(() => {
            window.location.replace('http://localhost:8082/index.html');
        }, 100);
    })
    .catch(error => {
        console.error('Erro ao fazer logout:', error);
        alert('Erro ao fazer logout. Tente novamente.');
    });
}

/**
 * Função para criar um novo pedido
 */
function criarPedido() {
    const nomeCliente = document.getElementById('nomeCliente').value.trim();
    const telefoneCliente = document.getElementById('telefoneCliente').value.trim();
    const enderecoCliente = document.getElementById('enderecoCliente').value.trim();
    const entrega = document.getElementById('entrega').checked;
    const observacoes = document.getElementById('observacoes').value.trim();
    const preco = parseFloat(document.getElementById('preco').value);

    if (!nomeCliente || !telefoneCliente || !preco) {
        alert('Por favor, preencha os campos obrigatórios!');
        return;
    }

    const pedido = {
        nomeCliente,
        telefoneCliente,
        enderecoCliente,
        entrega,
        observacoes,
        preco
    };

    fetch('/api/pedidos/criar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(pedido)
    })
    .then(response => {
        if (!response.ok) throw new Error('Erro ao criar pedido.');
        return response.json();
    })
    .then(() => {
        alert('Pedido criado com sucesso!');
        document.querySelector('form').reset();
    })
    .catch(error => {
        alert('Erro ao criar pedido!');
        console.error('Erro:', error);
    });
}

/**
 * Função para listar todos os pedidos
 */
function listarPedidos() {
    fetch('/api/pedidos/listar')
    .then(response => {
        if (!response.ok) throw new Error('Erro ao listar pedidos.');
        return response.json();
    })
    .then(data => {
        const pedidosList = document.getElementById('pedidosList').getElementsByTagName('tbody')[0];
        pedidosList.innerHTML = '';

        data.forEach(pedido => {
            const row = pedidosList.insertRow();
            row.innerHTML = `
                <td>${pedido.id}</td>
                <td>${pedido.nomeCliente}</td>
                <td>${pedido.telefoneCliente}</td>
                <td>${pedido.enderecoCliente}</td>
                <td>${pedido.preco.toFixed(2)}</td>
                <td>${new Date(pedido.dataPedido).toLocaleString()}</td>
                <td>${pedido.quantidade}</td>
            `;
        });
    })
    .catch(error => {
        alert('Erro ao listar pedidos!');
        console.error('Erro:', error);
    });
}

/**
 * Função para listar pedidos por período com detalhes
 */
function listarPedidosDaView() {
    const dataInicio = document.getElementById('dataInicio').value;
    const dataFim = document.getElementById('dataFim').value;

    if (!dataInicio || !dataFim) {
        alert('Por favor, preencha as datas corretamente!');
        return;
    }

    fetch(`/api/pedidos/listar-por-data?dataInicio=${dataInicio}&dataFim=${dataFim}`)
    .then(response => {
        if (!response.ok) throw new Error('Erro ao listar pedidos.');
        return response.json();
    })
    .then(data => {
        const pedidosViewList = document.getElementById('pedidosViewList').getElementsByTagName('tbody')[0];
        pedidosViewList.innerHTML = '';

        data.forEach(pedido => {
            const id = pedido.id || 'N/A';
            const nomeCliente = pedido.cliente ? pedido.cliente.nome : 'N/A';
            const enderecoCliente = pedido.enderecoCliente || 'N/A';
            const tipoEntrega = pedido.entrega ? 'Entrega' : 'Retirada';
            const preco = pedido.preco ? pedido.preco.toFixed(2) : 'N/A';
            const observacoes = pedido.observacoes || 'Sem observações';
            const dataPedido = pedido.dataPedido ? new Date(pedido.dataPedido).toLocaleString('pt-BR') : 'N/A';

            const itensPedido = pedido.itens && pedido.itens.length > 0
                ? pedido.itens.map(item => {
                    const produto = item.produto || {};
                    const nomeProduto = produto.nome || 'N/A';
                    const descricaoProduto = produto.descricao || 'Sem descrição disponível';
                    const precoUnitario = produto.preco ? produto.preco.toFixed(2) : '0.00';
                    const quantidade = item.quantidade || 1;
                    const precoTotalItem = (quantidade * (produto.preco || 0)).toFixed(2);

                    return `
                        <li class="item-detalhe">
                            <div>
                                <span><strong>Produto:</strong> ${nomeProduto}</span><br>
                                <span><strong>Descrição:</strong> ${descricaoProduto}</span><br>
                            </div>
                            <div>
                                <span><strong>Quantidade:</strong> ${quantidade}</span><br>
                                <span><strong>Preço Unitário:</strong> R$ ${precoUnitario}</span><br>
                                <span><strong>Total:</strong> R$ ${precoTotalItem}</span>
                            </div>
                        </li>
                    `;
                }).join('')
                : '<li class="item-detalhe vazio">Nenhum item encontrado</li>';

            const row = pedidosViewList.insertRow();
            row.innerHTML = `
                <td><strong>${id}</strong></td>
                <td>${dataPedido}</td>
                <td>${nomeCliente}</td>
                <td>${enderecoCliente}</td>
                <td>${tipoEntrega}</td>
                <td>
                    <div class="itens-pedido-container">
                        <ul class="lista-itens">${itensPedido}</ul>
                    </div>
                </td>
                <td><strong>R$ ${preco}</strong></td>
                <td>${observacoes}</td>
                <td>
                    <button class="btn-excluir" data-id="${id}">Excluir</button>
                </td>
            `;
        });
    })
    .catch(error => {
        alert(`Erro ao listar pedidos: ${error.message}`);
        console.error('Erro:', error);
    });
}

/**
 * Função para excluir um pedido
 * @param {string} id - ID do pedido a ser excluído
 */
function excluirPedido(id) {
    if (!id) {
        alert('ID do pedido não fornecido.');
        return;
    }

    if (!confirm(`Tem certeza que deseja excluir o pedido ${id}?`)) {
        return;
    }

    fetch(`/api/pedidos/excluir/${id}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) throw new Error('Erro ao excluir pedido.');
        return response.text();
    })
    .then(text => {
        if (text.trim() === "") {
            alert(`Pedido ${id} excluído com sucesso!`);
        } else {
            const data = JSON.parse(text);
            alert(`Pedido ${id} excluído: ${data.message}`);
        }
        listarPedidosDaView();
    })
    .catch(error => {
        alert('Erro ao excluir pedido!');
        console.error('Erro:', error);
    });
}

/**
 * Função para editar um pedido existente
 */
function editarPedido() {
    const pedidoId = document.getElementById('pedidoIdEditar').value.trim();
    const nomeCliente = document.getElementById('nomeClienteEditar').value.trim();
    const telefoneCliente = document.getElementById('telefoneClienteEditar').value.trim();
    const enderecoCliente = document.getElementById('enderecoClienteEditar').value.trim();
    const entrega = document.getElementById('entregaEditar').checked;
    const observacoes = document.getElementById('observacoesEditar').value.trim();
    const preco = parseFloat(document.getElementById('precoEditar').value);

    if (!pedidoId || !nomeCliente || !telefoneCliente || isNaN(preco)) {
        alert('Por favor, preencha os campos obrigatórios!');
        return;
    }

    const pedido = {
        nomeCliente,
        telefoneCliente,
        enderecoCliente,
        entrega,
        observacoes,
        preco
    };

    fetch(`/api/pedidos/editar/${pedidoId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(pedido)
    })
    .then(response => {
        if (!response.ok) throw new Error('Erro ao editar pedido.');
        return response.json();
    })
    .then(() => {
        alert('Pedido editado com sucesso!');
        document.querySelector('form').reset();
    })
    .catch(error => {
        alert('Erro ao editar pedido!');
        console.error('Erro:', error);
    });
}

/**
 * Função para gerar gráficos de pedidos
 * @param {Array} dados - Array de objetos de pedidos
 */
function gerarGraficos(dados) {
    if (!dados || dados.length === 0) {
        alert('Não há dados suficientes para gerar gráficos.');
        return;
    }

    const dias = {};
    dados.forEach(pedido => {
        const data = new Date(pedido.dataPedido).toLocaleDateString('pt-BR');
        dias[data] = (dias[data] || 0) + 1;
    });

    const labels = Object.keys(dias);
    const values = Object.values(dias);

    const ctx = document.getElementById('graficoPedidos').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Pedidos por Dia',
                data: values,
                backgroundColor: 'rgba(54, 162, 235, 0.7)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
                title: {
                    display: true,
                    text: 'Quantidade de Pedidos por Dia'
                }
            },
            scales: {
                y: { beginAtZero: true }
            }
        }
    });
}
