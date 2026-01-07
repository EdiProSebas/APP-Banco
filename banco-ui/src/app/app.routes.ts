import { Routes } from '@angular/router';
import { ClientesComponent } from './pages/clientes/ClientesComponent';
import { CuentasComponent } from './pages/cuentas/CuentasComponent';
import { MovimientosComponent } from './pages/movimientos/MovimientosComponent';
import { ReportesComponent } from './pages/reportes/ReportesComponent';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'clientes' },
  { path: 'clientes', component: ClientesComponent },
  { path: 'cuentas', component: CuentasComponent },
  { path: 'movimientos', component: MovimientosComponent },
  { path: 'reportes', component: ReportesComponent },
];