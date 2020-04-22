<template>
  <div>
    <nav-menu></nav-menu>
    <left-menu></left-menu>
    <div class="report-content" style="overflow-x: auto;">
      <div class="form-group">
          <label>Wybierz datę</label>
          <input 
            type="date" 
            id="date" 
            class="form-control" 
            v-model="date" 
            style="width: 160px;"
            @change="selectDate"/>
      </div>
      <div v-if="isDateSelected">
          <button class="btn btn-success" @click="generateMonthReport">Generuj miesięczny raport</button>
          <button class="btn btn-success" @click="generateYearReport">Generuj roczny raport</button>
      </div>

      <button 
        class="btn btn-success btn-primary btn-sm" 
        style="margin-top: 15px;"
        @click="displayReports">Pokaz wszystkie raporty</button>
      <div v-if="displayAllReports" class="all-reports">
       <table class="table table-striped">
          <thead>
            <tr>
              <th scope="col">Raport na</th>
              <th scope="col">Wartość sprzedanych zamówień</th>
              <th scope="col">Wartośc kupionych zamówień</th>
              <th scope="col">Sprzedana ilość (m3)</th>
              <th scope="col">Nieuzyta kwota kupców</th>
              <th scope="col">Nieuzyta kwota u dostawców</th>
              <th scope="col">Średnie kupno (m3/zl)</th>
              <th scope="col">Średnia sprzedaz (m3/zl)</th>
              <th scope="col">Sredni zarobek na m3</th>
              <th scope="col">Suma kosztów</th>
              <th scope="col">Zysk</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(report, index) in reports.slice().reverse()" v-bind:key="index">
              <td scope="row">{{ translateMonth(report.type) }}</td>
              <td scope="row">{{ report.soldedValue | toCurrency }}</td>
              <td scope="row">{{ report.boughtValue | toCurrency }}</td>
              <td scope="row">{{ report.soldedQuantity | toCurrency }}</td>
              <td scope="row">{{ report.buyersNotUsedValue | toCurrency }}</td>
              <td scope="row">{{ report.suppliersNotUsedValue | toCurrency }}</td>
              <td scope="row">{{ report.averagePurchase | toCurrency }}</td>
              <td scope="row">{{ report.averageSold | toCurrency }}</td>
              <td scope="row">{{ report.averageEarningsPerM3 | toCurrency }}</td>
              <td scope="row">{{ report.sumCosts | toCurrency }}</td>
              <td scope="row">{{ report.profit | toCurrency }}</td>
            </tr>
          </tbody>
        </table>
      </div>

  </div>
  </div>
</template>

<script>
import axios from '../axios-auth';
import store from '../store';
import NavMenu from "../NavMenu.vue";
import LeftMenu from "../LeftMenu.vue";

export default {
  components: {
    navMenu: NavMenu,
	  leftMenu: LeftMenu
  },
  data() {
    return {
      displayAllReports: false,
      reports: [],
      date: '',
      soldedValue: 0,
      boughtValue: 0,
      soldedQuantity: 0,
      buyersNotUsedValue: 0,
      suppliersNotUsedValue: 0,
      averageSold: 0,
      averagePurchase: 0,
      averageEarningsPerM3: 0,
      sumCosts: 0,
      profit: 0,
      type: "",
      isDateSelected: false
    };
  },
  methods: {
    generateMonthReport() {
      let date = this.getDate();
      const params = new URLSearchParams();
      params.append("localDate", this.date);
      axios.post("/report/generateMonthReport", params, {
        headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          }
      });
    },
    generateYearReport() {
      let date = this.getDate();
      const params = new URLSearchParams();
      params.append("localDate", this.date);
      axios.post("/report/generateYearReport", params, {
        headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          }
      });
    },
    getDate() {
      let currentDate = new Date();
      let currentDateWithFormat = new Date()
        .toJSON()
        .slice(0, 10)
        .replace(/-/g, "/");

      return currentDateWithFormat;
    },
    selectDate() {
      this.isDateSelected = true;
    },
    displayReports() {
      this.reports = [];
      axios.get("/report/getAll", {
        headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          }
      }).then(resp => {
      const data = resp.data;
      for (let key in data) {
        const report = data[key];
        report.id = report.id;
        this.reports.push(report);
      }
    });
      this.displayAllReports = !this.displayAllReports;
    },
    translateMonth(value) {
      if (value == 'JANUARY') {
        return 'Styczeń'
      }
      if (value == 'FEBRUARY') {
        return 'Luty'
      }
      if (value == 'MARCH') {
        return 'Marzec'
      }
      if (value == 'APRIL') {
        return 'Kwiecień'
      }
      if (value == 'MAY') {
        return 'Maj'
      }
      if (value == 'JUNE') {
        return 'Czerwiec'
      }
      if (value == 'JULY') {
        return 'Lipiec'
      }
      if (value == 'AUGUST') {
        return 'Sierpień'
      }
      if (value == 'SEPTEMBER') {
        return 'Wrzesień'
      }
      if (value == 'OCTOBER') {
        return 'Październik'
      }
      if (value == 'NOVEMBER') {
        return 'Listopad'
      }
      if (value == 'DECEMBER') {
        return 'Grudzień'
      }
      else {
        return value
      }
    }
  }
};
</script>

<style scoped>
.report-content {
  position: relative;
  width: 70%;
  height: 750px;
  margin-left: 400px;
  border: 1px solid black;
  border-radius: 5px;
  padding: 5px;
}

.values-content {
  padding: 5px;
}

table {
  width: 2000px;
}
</style>