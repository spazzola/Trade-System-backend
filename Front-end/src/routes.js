import About from './company-view/About.vue'
import Offer from './company-view/Offer.vue'
import Gallery from './company-view/Gallery.vue'
import Contact from './company-view/Contact.vue'
import Login from './application-view/Login.vue'
import Order from './application-view/orders/Orders.vue'
import Invoice from './application-view/invoices/invoices.vue'
import Buyer from './application-view/buyer-panel/Buyer.vue'
import Supplier from './application-view/supplier-panel/Supplier.vue'
import Product from './application-view/product-panel/Product.vue'
import Report from './application-view/report-panel/Report.vue'

import store from './store'

export const routes = [
    {path: '/', component: About},
    {path: '/offer', component: Offer},
    {path: '/about', component: About},
    {path: '/gallery', component: Gallery},
    {path: '/contact', component: Contact},
    {path: '/login', component: Login},
    {
        path: '/orders', 
        component: Order, 
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
        component: Invoice,
        beforeEnter(to, from, next) {
            if (store.state.jwt) {
                next();
            } else {
                next('/login')
            }
        }
    },
    {
        path: '/buyer',
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
        path: '/supplier',
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
        path: '/report',
        component: Report,
        beforeEnter(to, from, next) {
            if (store.state.jwt) {
                next();
            } else {
                next('/login')
            }
        }
    }
];