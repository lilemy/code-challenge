export default [
  {
    path: '/user',
    layout: false,
    routes: [
      { name: '登录', path: '/user/login', component: './User/Login' },
      {
        name: '注册',
        path: '/user/register',
        component: './User/Register',
      },
    ],
  },
  { path: '/welcome', name: '欢迎', icon: 'smile', component: './Welcome' },
  { path: '/banks', name: '题库', icon: 'table', component: './Bank' },
  {
    path: '/bank/:questionBankId',
    name: '题库详情',
    component: './Bank/BankDetail',
    hideInMenu: true,
  },
  {
    path: '/bank/:questionBankId/question/:questionId',
    name: '题目详情',
    component: './Bank/BankQuestionDetail',
    hideInMenu: true,
  },
  { path: '/questions', name: '题目', icon: 'bars', component: './Question' },
  {
    path: '/question/:questionId',
    name: '题目详情',
    component: './Question/QuestionDetail',
    hideInMenu: true,
  },
  {
    path: '/question/create',
    name: '创建题目',
    component: './Question/QuestionCreate',
    hideInMenu: true,
  },
  {
    path: 'account/center',
    name: '个人中心',
    component: './Account/Center',
    hideInMenu: true,
  },
  {
    path: 'account/setting',
    name: '个人设置',
    component: './Account/Setting',
    hideInMenu: true,
  },
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
      {
        path: '/admin/review/questions',
        name: '题目审核',
        component: './Admin/Review/Question',
        hideInMenu: true,
      },
    ],
  },
  { path: '/', redirect: '/welcome' },
  { path: '*', layout: false, component: './404' },
];
