export default [
  {
    path: '/user',
    layout: false,
    routes: [{ name: '登录', path: '/user/login', component: './User/Login' }],
  },
  { path: '/welcome', name: '欢迎', icon: 'smile', component: './Welcome' },
  { path: '/banks', name: '题库', icon: 'table', component: './Bank' },
  { path: '/questions', name: '题目', icon: 'bars', component: './Question' },
  {
    path: '/admin',
    name: '管理页',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      { path: '/admin', redirect: '/admin/users' },
      { path: '/admin/users', name: '用户管理', component: './Admin/User' },
      { path: '/admin/banks', name: '题库管理', component: './Admin/QuestionBank' },
      { path: '/admin/questions', name: '题目管理', component: './Admin/Question' },
    ],
  },
  { path: '/', redirect: '/welcome' },
  { path: '*', layout: false, component: './404' },
];
