import request from '@/utils/request'
//注册
export const userRegisiter = (registerData) => {
  //借助于UrlSearchParams完成传递
  const params = new URLSearchParams()
  for (let key in registerData) {
    params.append(key, registerData[key])
  }
  return request.post('/user/register', params)
}
//注册
export const userLogin = (loginData) => {
  //借助于UrlSearchParams完成传递
  const params = new URLSearchParams()
  for (let key in loginData) {
    params.append(key, loginData[key])
  }
  return request.post('/user/login', params)
}
//获取用户信息
export const userGetInfo = () => request.get('/user/userinfo')
//更新用户信息
export const userUpdate = (data) => request.put('/user/update', data)
//上传头像
export const userAvatar = (avatarUrl) =>
  request.put(`/user/avatar?avatarUrl=${avatarUrl}`)
//重置密码
export const userPwd = (data) => {
  const params = new URLSearchParams()
  for (let key in data) {
    params.append(key, data[key])
  }
  return request.patch('/user/password', params)
}
