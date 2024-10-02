declare namespace API {
  type BaseResponseBoolean = {
    code?: number;
    data?: boolean;
    message?: string;
  };

  type BaseResponseLoginUserVO = {
    code?: number;
    data?: LoginUserVO;
    message?: string;
  };

  type BaseResponseLong = {
    code?: number;
    data?: number;
    message?: string;
  };

  type BaseResponsePageQuestion = {
    code?: number;
    data?: PageQuestion;
    message?: string;
  };

  type BaseResponsePageQuestionBank = {
    code?: number;
    data?: PageQuestionBank;
    message?: string;
  };

  type BaseResponsePageQuestionBankQuestionVO = {
    code?: number;
    data?: PageQuestionBankQuestionVO;
    message?: string;
  };

  type BaseResponsePageQuestionBankVO = {
    code?: number;
    data?: PageQuestionBankVO;
    message?: string;
  };

  type BaseResponsePageQuestionVO = {
    code?: number;
    data?: PageQuestionVO;
    message?: string;
  };

  type BaseResponsePageUser = {
    code?: number;
    data?: PageUser;
    message?: string;
  };

  type BaseResponsePageUserVO = {
    code?: number;
    data?: PageUserVO;
    message?: string;
  };

  type BaseResponseQuestionBankQuestionVO = {
    code?: number;
    data?: QuestionBankQuestionVO;
    message?: string;
  };

  type BaseResponseQuestionBankVO = {
    code?: number;
    data?: QuestionBankVO;
    message?: string;
  };

  type BaseResponseQuestionVO = {
    code?: number;
    data?: QuestionVO;
    message?: string;
  };

  type BaseResponseUser = {
    code?: number;
    data?: User;
    message?: string;
  };

  type BaseResponseUserVO = {
    code?: number;
    data?: UserVO;
    message?: string;
  };

  type DeleteRequest = {
    id?: number;
  };

  type getQuestionBankQuestionVOByIdParams = {
    id: number;
  };

  type getQuestionBankVOByIdParams = {
    id: number;
    isNeedQueryQuestionList: boolean;
  };

  type getQuestionVOByIdParams = {
    id: number;
  };

  type getUserByIdParams = {
    id: number;
  };

  type getUserVOByIdParams = {
    id: number;
  };

  type LoginUserVO = {
    id?: number;
    username?: string;
    userAccount?: string;
    userAvatar?: string;
    userProfile?: string;
    userRole?: string;
    editTime?: string;
    updateTime?: string;
    createTime?: string;
  };

  type OrderItem = {
    column?: string;
    asc?: boolean;
  };

  type PageQuestion = {
    records?: Question[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageQuestion;
    searchCount?: PageQuestion;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageQuestionBank = {
    records?: QuestionBank[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageQuestionBank;
    searchCount?: PageQuestionBank;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageQuestionBankQuestionVO = {
    records?: QuestionBankQuestionVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageQuestionBankQuestionVO;
    searchCount?: PageQuestionBankQuestionVO;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageQuestionBankVO = {
    records?: QuestionBankVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageQuestionBankVO;
    searchCount?: PageQuestionBankVO;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageQuestionVO = {
    records?: QuestionVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageQuestionVO;
    searchCount?: PageQuestionVO;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageUser = {
    records?: User[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageUser;
    searchCount?: PageUser;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageUserVO = {
    records?: UserVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageUserVO;
    searchCount?: PageUserVO;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type Question = {
    id?: number;
    title?: string;
    content?: string;
    tags?: string;
    answer?: string;
    userId?: number;
    reviewStatus?: number;
    reviewMessage?: string;
    reviewerId?: number;
    reviewTime?: string;
    viewNum?: number;
    thumbNum?: number;
    favourNum?: number;
    priority?: number;
    editTime?: string;
    createTime?: string;
    updateTime?: string;
    isDelete?: number;
  };

  type QuestionAddRequest = {
    title?: string;
    content?: string;
    tags?: string[];
    answer?: string;
  };

  type QuestionBank = {
    id?: number;
    title?: string;
    description?: string;
    picture?: string;
    priority?: number;
    userId?: number;
    editTime?: string;
    createTime?: string;
    updateTime?: string;
    isDelete?: number;
  };

  type QuestionBankAddRequest = {
    title?: string;
    description?: string;
    picture?: string;
  };

  type QuestionBankQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    notId?: number;
    searchText?: string;
    title?: string;
    description?: string;
    picture?: string;
    userId?: number;
    needQueryQuestionList?: boolean;
  };

  type QuestionBankQuestionAddRequest = {
    questionBankId?: number;
    questionId?: number;
  };

  type QuestionBankQuestionQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    notId?: number;
    questionBankId?: number;
    questionId?: number;
    userId?: number;
  };

  type QuestionBankQuestionRemoveRequest = {
    questionBankId?: number;
    questionId?: number;
  };

  type QuestionBankQuestionUpdateRequest = {
    id?: number;
    questionBankId?: number;
    questionId?: number;
  };

  type QuestionBankQuestionVO = {
    id?: number;
    questionBankId?: number;
    questionId?: number;
    userId?: number;
    createTime?: string;
    updateTime?: string;
    tagList?: string[];
    user?: UserVO;
  };

  type QuestionBankUpdateRequest = {
    id?: number;
    title?: string;
    description?: string;
    picture?: string;
    priority?: number;
  };

  type QuestionBankVO = {
    id?: number;
    title?: string;
    description?: string;
    picture?: string;
    userId?: number;
    createTime?: string;
    updateTime?: string;
    user?: UserVO;
    questionPage?: PageQuestionVO;
  };

  type QuestionEditRequest = {
    id?: number;
    title?: string;
    content?: string;
    tags?: string[];
    answer?: string;
  };

  type QuestionQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    notId?: number;
    searchText?: string;
    title?: string;
    content?: string;
    tags?: string[];
    answer?: string;
    questionBankId?: number;
    userId?: number;
  };

  type QuestionUpdateRequest = {
    id?: number;
    title?: string;
    content?: string;
    tags?: string[];
    priority?: number;
    answer?: string;
    reviewStatus?: number;
    reviewMessage?: string;
  };

  type QuestionVO = {
    id?: number;
    title?: string;
    content?: string;
    answer?: string;
    userId?: number;
    createTime?: string;
    updateTime?: string;
    tagList?: string[];
    user?: UserVO;
  };

  type User = {
    id?: number;
    userAccount?: string;
    userPassword?: string;
    unionId?: string;
    mpOpenId?: string;
    username?: string;
    userAvatar?: string;
    userProfile?: string;
    userRole?: string;
    editTime?: string;
    createTime?: string;
    updateTime?: string;
    isDelete?: number;
  };

  type UserAddRequest = {
    userAccount?: string;
    userRole?: string;
    username?: string;
  };

  type UserLoginRequest = {
    userAccount?: string;
    userPassword?: string;
  };

  type UserQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    username?: string;
    userAccount?: string;
    userRole?: string;
  };

  type UserRegisterRequest = {
    userAccount?: string;
    userPassword?: string;
    checkPassword?: string;
  };

  type UserUpdateRequest = {
    id?: number;
    username?: string;
    userAccount?: string;
    userPassword?: string;
    userAvatar?: string;
    userProfile?: string;
    userRole?: string;
  };

  type UserVO = {
    id?: number;
    userAccount?: string;
    username?: string;
    userAvatar?: string;
    userProfile?: string;
    userRole?: string;
    createTime?: string;
  };
}
