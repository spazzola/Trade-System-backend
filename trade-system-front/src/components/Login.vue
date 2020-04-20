<template>
    <div class="login-content">

        <form>
            <div class="form-group">
                <input type="text" v-model="login" class="form-control" placeholder="Login">
            </div>

            <div>
                <input type="password" v-model="password" class="form-control" placeholder="Hasło">

                <button type="submit" @click.stop.prevent="submit()" class="btn btn-success btn-primary btn-md">Zaloguj</button>
            </div>
            
        </form>

        <footer> &copy; 2020 Created by Rafal Szczotka. All rights reserved.</footer>
    </div>
</template>

<script>

export default {
    data() {
        return {
            login: '',
            password: ''
        }
    },
    methods: {
        submit() {
            const formData = {
                login: this.login,
                password: this.password
            };
            this.$store.dispatch('login', {
                login: formData.login,
                password: formData.password
            }).then(this.$router.push("/orders"));
        }
    }
}
</script>

<style scoped>

.login-content {
    text-align: center;
    margin-top: 300px;
}
.form-control {
  width: 170px;
  height: 35px;
  margin: auto;
}

.btn {
    margin-top: 15px;
}

footer {
    margin-top:200px;
}
</style>