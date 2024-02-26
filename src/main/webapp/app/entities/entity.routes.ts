import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'persona',
    data: { pageTitle: 'reparacionesApp.persona.home.title' },
    loadChildren: () => import('./persona/persona.routes'),
  },
  {
    path: 'funcionalidad',
    data: { pageTitle: 'reparacionesApp.funcionalidad.home.title' },
    loadChildren: () => import('./funcionalidad/funcionalidad.routes'),
  },
  {
    path: 'catalogo',
    data: { pageTitle: 'reparacionesApp.catalogo.home.title' },
    loadChildren: () => import('./catalogo/catalogo.routes'),
  },
  {
    path: 'catalogo-item',
    data: { pageTitle: 'reparacionesApp.catalogoItem.home.title' },
    loadChildren: () => import('./catalogo-item/catalogo-item.routes'),
  },
  {
    path: 'parametro-sistema',
    data: { pageTitle: 'reparacionesApp.parametroSistema.home.title' },
    loadChildren: () => import('./parametro-sistema/parametro-sistema.routes'),
  },
  {
    path: 'rol-funcionalidad',
    data: { pageTitle: 'reparacionesApp.rolFuncionalidad.home.title' },
    loadChildren: () => import('./rol-funcionalidad/rol-funcionalidad.routes'),
  },
  {
    path: 'registro-danio',
    data: { pageTitle: 'reparacionesApp.registroDanio.home.title' },
    loadChildren: () => import('./registro-danio/registro-danio.routes'),
  },
  {
    path: 'detalle-danio',
    data: { pageTitle: 'reparacionesApp.detalleDanio.home.title' },
    loadChildren: () => import('./detalle-danio/detalle-danio.routes'),
  },
  {
    path: 'material-danio',
    data: { pageTitle: 'reparacionesApp.materialDanio.home.title' },
    loadChildren: () => import('./material-danio/material-danio.routes'),
  },
  {
    path: 'machinery',
    data: { pageTitle: 'reparacionesApp.machinery.home.title' },
    loadChildren: () => import('./machinery/machinery.routes'),
  },
  {
    path: 'detalle-reporte-danio',
    data: { pageTitle: 'reparacionesApp.detalleReporteDanio.home.title' },
    loadChildren: () => import('./detalle-reporte-danio/detalle-reporte-danio.routes'),
  },
  {
    path: 'material-reporte-control',
    data: { pageTitle: 'reparacionesApp.materialReporteControl.home.title' },
    loadChildren: () => import('./material-reporte-control/material-reporte-control.routes'),
  },
  {
    path: 'material',
    data: { pageTitle: 'reparacionesApp.material.home.title' },
    loadChildren: () => import('./material/material.routes'),
  },
  {
    path: 'orden-bodega',
    data: { pageTitle: 'reparacionesApp.ordenBodega.home.title' },
    loadChildren: () => import('./orden-bodega/orden-bodega.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
