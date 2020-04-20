import Orders from './components/Orders.vue'
import Invoices from './components/Invoices.vue'
import App from './App.vue'
import Buyer from './components/Buyer.vue'
import Supplier from './components/Supplier.vue'
import Report from './components/Report'
import AddInvoice from './components/AddInvoice.vue'
import PayInvoice from './components/PayInvoice.vue'
import AddOrder from './components/AddOrder.vue'
import Product from './components/Product.vue'
import Price from './components/Price.vue'
import Costs from './components/Costs.vue'
import Login from './components/Login.vue'

import store from './store'

export const routes = [
    {path: '/', component: Login},
    // {path: '/', component: App},
    {
        path: '/orders', 
        component: Orders, 
        beforeEnter(to, from, next) {
            if (store.state.jwt) {
                next();
            } else {
                next('/login')
            }
        }
    },
    {
        path: '/addOrder', 
        component: AddOrder,
        beforeEnter(to, from, next) {
            if (store.state.jwt) {
                next();
            } else {
                next('/login')
            }
        }
    },
    {
        path: '/invoices', 
        component: Invoices,
        beforeEnter(to, from, next) {
            if (store.state.jwt) {
                next();
            } else {
                next('/login')
            }
        }
    },
    {
        path: '/addInvoice', 
        component: AddInvoice,
        beforeEnter(to, from, next) {
            if (store.state.jwt) {
                next();
            } else {
                next('/login')
            }
        }
    },
    {
        path: '/payInvoice', 
        component: PayInvoice,
        beforeEnter(to, from, next) {
            if (store.state.jwt) {
                next();
            } else {
                next('/login')
            }
        }
    },
    {
        path: '/generateReport', 
        component: Report,
        beforeEnter(to, from, next) {
            if (store.state.jwt) {
                next();
            } else {
                next('/login')
            }
        }},
    {
        path: '/addBuyer', 
        component: Buyer,
        beforeEnter(to, from, next) {
            if (store.state.jwt) {
                next();
            } else {
                next('/login')
            }
        }
    },
    {
        path: '/addSupplier', 
        component: Supplier,
        beforeEnter(to, from, next) {
            if (store.state.jwt) {
                next();
            } else {
                next('/login')
            }
        }
    },
    {
        path: '/product', 
        component: Product,
        beforeEnter(to, from, next) {
            if (store.state.jwt) {
                next();
            } else {
                next('/login')
            }
        }
    },
    {
        path: '/price', 
        component: Price,
        beforeEnter(to, from, next) {
            if (store.state.jwt) {
                next();
            } else {
                next('/login')
            }
        }
    },
    {
        path: '/costs', 
        component: Costs, 
        beforeEnter(to, from, next) {
            if (store.state.jwt) {
                next();
            } else {
                next('/login')
            }
        }},
    {path: '*', component: Login}
];