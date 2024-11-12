-- 创建数据库
create schema if not exists code_challenge collate utf8mb4_unicode_ci;

-- 使用数据库
use code_challenge;

-- 用户表
create table if not exists user
(
    `id`            bigint auto_increment comment 'id' primary key,
    `user_account`  varchar(256)                           not null comment '账号',
    `user_password` varchar(512)                           not null comment '密码',
    `union_id`      varchar(256)                           null comment '微信开放平台id',
    `mp_open_id`    varchar(256)                           null comment '公众号openId',
    `username`      varchar(256)                           null comment '用户昵称',
    `user_avatar`   varchar(1024)                          null comment '用户头像',
    `user_profile`  varchar(512)                           null comment '用户简介',
    `user_role`     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    `edit_time`     datetime     default CURRENT_TIMESTAMP not null comment '编辑时间',
    `create_time`   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete`     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (union_id)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 题库表
create table if not exists question_bank
(
    `id`          bigint auto_increment comment 'id' primary key,
    `title`       varchar(256)                       null comment '标题',
    `description` text                               null comment '描述',
    `picture`     varchar(2048)                      null comment '图片',
    `priority`    int      default 0                 not null comment '优先级',
    `user_id`     bigint                             not null comment '创建用户 id',
    `edit_time`   datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete`   tinyint  default 0                 not null comment '是否删除',
    index idx_title (title)
) comment '题库' collate = utf8mb4_unicode_ci;

-- 题目表
create table if not exists question
(
    `id`             bigint auto_increment comment 'id' primary key,
    `title`          varchar(256)                       null comment '标题',
    `content`        text                               null comment '内容',
    `tags`           varchar(1024)                      null comment '标签列表（json 数组）',
    `answer`         text                               null comment '推荐答案',
    `user_id`        bigint                             not null comment '创建用户 id',
    `review_status`  int      default 0                 not null comment '状态：0-待审核, 1-通过, 2-拒绝',
    `review_message` varchar(512)                       null comment '审核信息',
    `reviewer_id`    bigint                             null comment '审核人 id',
    `review_time`    datetime                           null comment '审核时间',
    `view_num`       int      default 0                 not null comment '浏览量',
    `thumb_num`      int      default 0                 not null comment '点赞数',
    `favour_num`     int      default 0                 not null comment '收藏数',
    `priority`       int      default 0                 not null comment '优先级',
    `edit_time`      datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    `create_time`    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete`      tinyint  default 0                 not null comment '是否删除',
    index idx_title (title),
    index idx_user_id (user_id),
    index idx_review_status (review_status)
) comment '题目' collate = utf8mb4_unicode_ci;

-- 题库题目表
create table if not exists question_bank_question
(
    `id`               bigint auto_increment comment 'id' primary key,
    `question_bank_id` bigint                             not null comment '题库 id',
    `question_id`      bigint                             not null comment '题目 id',
    `user_id`          bigint                             not null comment '创建用户 id',
    `create_time`      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    UNIQUE (question_bank_id, question_id)
) comment '题库题目' collate = utf8mb4_unicode_ci;

-- 笔记表
create table if not exists note
(
    `id`             bigint auto_increment comment 'id' primary key,
    `title`          varchar(512)                       null comment '标题',
    `content`        text                               null comment '内容',
    `tags`           varchar(1024)                      null comment '标签列表（json 数组）',
    `picture`        varchar(2048)                      null comment '图片',
    `thumb_num`      int      default 0                 not null comment '点赞数',
    `favour_num`     int      default 0                 not null comment '收藏数',
    `view_num`       int      default 0                 not null comment '浏览量',
    `review_status`  int      default 0                 not null comment '状态：0-待审核, 1-通过, 2-拒绝',
    `review_message` varchar(512)                       null comment '审核信息',
    `reviewer_id`    bigint                             null comment '审核人 id',
    `review_time`    datetime                           null comment '审核时间',
    `user_id`        bigint                             not null comment '创建用户 id',
    `visible`        tinyint  DEFAULT 0                 not null comment '可见范围(0-公开, 1-仅对自己可见)',
    `is_top`         tinyint  DEFAULT 0                 not null comment '是否置顶(0-未置顶 1-置顶)',
    `edit_time`      datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    `create_time`    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete`      tinyint  default 0                 not null comment '是否删除',
    index idx_user_id (user_id),
    index idx_review_status (review_status)
) comment '笔记' collate = utf8mb4_unicode_ci;

-- 笔记点赞表（硬删除）
create table if not exists note_thumb
(
    `id`          bigint auto_increment comment 'id' primary key,
    `note_id`     bigint                             not null comment '笔记 id',
    `user_id`     bigint                             not null comment '创建用户 id',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_note_id (note_id),
    index idx_user_id (user_id)
) comment '笔记点赞';

-- 笔记收藏表（硬删除）
create table if not exists note_favour
(
    `id`          bigint auto_increment comment 'id' primary key,
    `note_id`     bigint                             not null comment '笔记 id',
    `user_id`     bigint                             not null comment '创建用户 id',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_note_id (note_id),
    index idx_user_id (user_id)
) comment '笔记收藏';

-- 笔记分类表
create table if not exists categories
(
    `id`          bigint auto_increment comment 'id' primary key,
    `name`        varchar(256)                       not null comment '笔记 id',
    `priority`    int      default 0                 not null comment '优先级',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete`   tinyint  default 0                 not null comment '是否删除',
    index idx_name (name)
) comment '笔记分类' collate = utf8mb4_unicode_ci;

-- 笔记分类关系表
create table if not exists note_categories
(
    `id`            bigint auto_increment comment 'id' primary key,
    `note_id`       bigint                             not null comment '笔记 id',
    `categories_id` bigint                             not null comment '笔记分类 id',
    `user_id`       bigint                             not null comment '创建用户 id',
    `create_time`   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    UNIQUE (categories_id, note_id)
) comment '笔记分类关系' collate = utf8mb4_unicode_ci;