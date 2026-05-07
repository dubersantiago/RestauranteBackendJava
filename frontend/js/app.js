const API = "http://localhost:8080";

let productos = [];
let carrito = [];
let categoriaActiva = "todas";

async function init() {
    await cargarCategorias();
    await cargarProductos();
}

async function cargarCategorias() {
    const res = await fetch(`${API}/categorias`);
    const cats = await res.json();
    const cont = document.getElementById("categorias");
    cats.forEach(c => {
        const btn = document.createElement("button");
        btn.className = "cat-btn";
        btn.textContent = c.nombre;
        btn.onclick = () => filtrarCategoria(c.nombre);
        cont.appendChild(btn);
    });
}

async function cargarProductos() {
    const res = await fetch(`${API}/productos`);
    productos = await res.json();
    renderProductos();
}

function filtrarCategoria(cat) {
    categoriaActiva = cat;
    document.querySelectorAll(".cat-btn").forEach(b => {
        b.classList.toggle("active", b.textContent === cat || (cat === "todas" && b.textContent === "Todas"));
    });
    renderProductos();
}

function renderProductos() {
    const cont = document.getElementById("productos");
    cont.innerHTML = "";
    const filtrados = categoriaActiva === "todas"
        ? productos
        : productos.filter(p => p.categoria === categoriaActiva);

    filtrados.forEach(p => {
        const card = document.createElement("div");
        card.className = "card";
        card.innerHTML = `
            <h3>${p.nombre}</h3>
            <span class="cat">${p.categoria}</span>
            <span class="precio">$${p.precio}</span>
            <span class="stock">Stock: ${p.stock}</span>
            <button onclick="agregarAlCarrito(${p.id})">Agregar</button>
        `;
        cont.appendChild(card);
    });
}

function agregarAlCarrito(id) {
    const prod = productos.find(p => p.id === id);
    if (!prod) return;

    const enCarrito = carrito.find(c => c.id === id);
    if (enCarrito) {
        if (enCarrito.cantidad < prod.stock) {
            enCarrito.cantidad++;
        } else {
            mostrarModal("No hay mas stock disponible");
            return;
        }
    } else {
        carrito.push({ id: prod.id, nombre: prod.nombre, precio: prod.precio, cantidad: 1 });
    }
    renderCarrito();
}

function quitarDelCarrito(id) {
    const item = carrito.find(c => c.id === id);
    if (item) {
        item.cantidad--;
        if (item.cantidad === 0) {
            carrito = carrito.filter(c => c.id !== id);
        }
    }
    renderCarrito();
}

function renderCarrito() {
    const cont = document.getElementById("cart-items");
    cont.innerHTML = "";
    let total = 0;
    let count = 0;

    carrito.forEach(item => {
        const sub = item.precio * item.cantidad;
        total += sub;
        count += item.cantidad;

        const div = document.createElement("div");
        div.className = "cart-item";
        div.innerHTML = `
            <span>${item.nombre} x${item.cantidad}</span>
            <span>$${sub}</span>
            <button onclick="quitarDelCarrito(${item.id})">-</button>
        `;
        cont.appendChild(div);
    });

    document.getElementById("cart-total").textContent = total.toFixed(2);
    document.getElementById("cart-count").textContent = count;
}

function toggleCart() {
    document.getElementById("cart").classList.toggle("cart-hidden");
}

async function crearPedido() {
    if (carrito.length === 0) {
        mostrarModal("El carrito esta vacio");
        return;
    }

    const detalles = carrito.map(c => ({
        productoId: c.id,
        cantidad: c.cantidad
    }));

    try {
        const res = await fetch(`${API}/pedidos`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ detalles })
        });

        if (!res.ok) {
            const err = await res.json();
            mostrarModal(err.error || "Error al crear el pedido");
            return;
        }

        mostrarModal("Pedido creado exitosamente!");
        carrito = [];
        renderCarrito();
        toggleCart();
        await cargarProductos();
    } catch (e) {
        mostrarModal("Error de conexion con el servidor");
    }
}

function mostrarModal(msg) {
    document.getElementById("modal-msg").textContent = msg;
    document.getElementById("modal").classList.remove("modal-hidden");
}

function cerrarModal() {
    document.getElementById("modal").classList.add("modal-hidden");
}

init();
