// Função para alternar a visibilidade das seções
function toggleSection(sectionId) {
    // Esconde todas as seções
    const sections = document.querySelectorAll('section');
    sections.forEach(section => {
        section.classList.add('hidden-section');
    });

    // Exibe a seção selecionada
    const sectionToShow = document.getElementById(sectionId);
    if (sectionToShow) {
        sectionToShow.classList.remove('hidden-section');
    } else {
        console.error(`Seção com ID '${sectionId}' não encontrada.`);
    }
}



function logout() {
    console.log('Fazendo logout...');

    // Fazer uma requisição POST para o endpoint de logout no servidor
    fetch('/api/auth/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) throw new Error('Erro ao fazer logout.');
        return response.text(); // Ou response.json(), dependendo do que seu endpoint retorna
    })
    .then(() => {
        console.log('Logout bem-sucedido.');

        // Remover o token JWT do cookie e do localStorage
        document.cookie = 'token=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/';  // Remove o cookie
        localStorage.removeItem('token'); // Caso o token esteja no localStorage

        // Redirecionar para a página de login após um pequeno delay
        setTimeout(function() {
            window.location.replace('http://localhost:8082/index.html'); // URL da página de login
        }, 100); // Adiciona um pequeno delay para garantir a remoção do token
    })
    .catch(error => {
        console.error('Erro ao fazer logout:', error);
        alert('Erro ao fazer logout. Tente novamente.');
    });
}







// Função para criar pedido
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
    .then(data => {
        alert('Pedido criado com sucesso!');
        document.querySelector('form').reset();
    })
    .catch(error => {
        alert('Erro ao criar pedido!');
        console.error(error);
    });
}

// Função para listar pedidos
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
            console.error(error);
        });
}

// Função para listar pedidos por data
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
            return response.json(); // Alterado para retornar JSON diretamente
        })
        .then(data => {
            const pedidosViewList = document.getElementById('pedidosViewList').getElementsByTagName('tbody')[0];
            pedidosViewList.innerHTML = '';

            data.forEach(pedido => {
                const id = pedido.id;
                if (!id) {
                    console.warn("Pedido sem ID, ignorado.");
                    return;
                }

                const nomeCliente = pedido.cliente ? pedido.cliente.nome : 'N/A';
                const enderecoCliente = pedido.enderecoCliente || 'N/A';
                const tipoEntrega = pedido.entrega ? 'Entrega' : 'Retirada';
                const preco = pedido.preco ? pedido.preco.toFixed(2) : 'N/A';
                const observacoes = pedido.observacoes || '';
                const dataPedido = pedido.dataPedido ? new Date(pedido.dataPedido).toLocaleString() : 'N/A';

                let itensPedido = pedido.itens ? pedido.itens.map(item => {
                    const nomeProduto = item.produto && item.produto.nome ? item.produto.nome : 'N/A';
                    const precoItem = item.preco ? item.preco.toFixed(2) : 'N/A';
                    return `<li>${nomeProduto} - R$ ${precoItem}</li>`;
                }).join('') : '<li>Nenhum item</li>';

                const row = pedidosViewList.insertRow();
                row.innerHTML = `
                    <td>${id}</td>
                    <td>${dataPedido}</td>
                    <td>${nomeCliente}</td>
                    <td>${enderecoCliente}</td>
                    <td>${tipoEntrega}</td>
                    <td><ul>${itensPedido}</ul></td>
                    <td>R$ ${preco}</td>
                    <td>${observacoes}</td>
                    <td>
                        <button class="btn-excluir" data-id="${id}">Excluir</button>
                    </td>
                `;
            });
        })
        .catch(error => {
            alert(`Erro ao listar pedidos: ${error.message}`);
            console.error(error);
        });
}

// EVENT DELEGATION PARA O BOTÃO EXCLUIR
document.getElementById('pedidosViewList').addEventListener('click', function (event) {
    if (event.target.classList.contains('btn-excluir')) {
        const pedidoId = event.target.dataset.id; // Captura o ID do atributo data-id
        console.log("ID capturado para exclusão:", pedidoId);
        
        if (!pedidoId) {
            alert("Erro: ID do pedido não encontrado!");
            return;
        }

        excluirPedido(pedidoId);
    }
});

function excluirPedido(id) {
    if (!id) {
        alert('Por favor, insira o ID do pedido para exclusão.');
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
        return response.text(); // Alterado de response.json() para response.text()
    })
    .then(text => {
        if (text.trim() === "") {
            alert(`Pedido ${id} excluído com sucesso!`);
        } else {
            const data = JSON.parse(text);
            alert(`Pedido ${id} excluído: ${data.message}`);
        }
        listarPedidosDaView(); // Atualiza a lista após a exclusão
    })
    .catch(error => {
        alert('Erro ao excluir pedido!');
        console.error(error);
    });
}

// Função para editar pedido
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
    .then(data => {
        alert('Pedido editado com sucesso!');
        document.querySelector('form').reset();
    })
    .catch(error => {
        alert('Erro ao editar pedido!');
       
		 console.error(error);
		 
		 
		 
		 
		 
		 // Função para gerar gráficos usando Chart.js
		 function gerarGraficos(dados) {
		     const ctx = document.getElementById('graficoPedidos').getContext('2d');
		     
		     const labels = dados.map(pedido => new Date(pedido.dataPedido).toLocaleDateString());
		     const valores = dados.map(pedido => pedido.preco);
		     
		     new Chart(ctx, {
		         type: 'bar',
		         data: {
		             labels: labels,
		             datasets: [{
		                 label: 'Valor dos Pedidos',
		                 data: valores,
		                 backgroundColor: 'rgba(75, 192, 192, 0.2)',
		                 borderColor: 'rgba(75, 192, 192, 1)',
		                 borderWidth: 1
		             }]
		         },
		         options: {
		             responsive: true,
		             scales: {
		                 y: {
		                     beginAtZero: true
		                 }
		             }
		         }
		     });
		 }

		 // Função para buscar pedidos e gerar gráficos
		 function listarPedidosComGrafico() {
		     fetch('/api/pedidos/listar')
		         .then(response => {
		             if (!response.ok) throw new Error('Erro ao listar pedidos.');
		             return response.json();
		         })
		         .then(data => {
		             gerarGraficos(data); // Gera os gráficos com os pedidos recebidos
		         })
		         .catch(error => {
		             alert('Erro ao listar pedidos!');
		             console.error(error);
		         });
		 }

		 // Chamar a função ao carregar a página
		 window.onload = function () {
		     listarPedidosComGrafico();
		 };

		 
		 
		 
		 
		 
		 
		 
    });
}
