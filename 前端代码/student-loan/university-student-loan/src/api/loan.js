import request from '@/utils/request'
//贷款申请
export const loanApply = (data) => request.post('/loan/apply', data)
//学生获取贷款列表
export const StudentList = () => request.get('/loan/student')
//获取贷款信息
export const loanDetail = (id) =>
  request.get('/loan/detail', {
    params: {
      id
    }
  })
//更新贷款内容
export const updateUserLoan = (data) => request.put('/loan/update', data)
//删除
export const deleteLoan = (id) =>
  request.delete('/loan/delete', {
    params: {
      id
    }
  })
//校方或管理人员获取贷款列表
export const schoolList = ({ pageNum, pageSize }) =>
  request.get('/loan/school', { params: { pageNum, pageSize } })
//审核通过
export const loanAgree = (id) => request.put(`/loan/agree?id=${id}`)
//审核不通过
export const loanDisAgree = (id) => request.put(`/loan/disagree?id=${id}`)
