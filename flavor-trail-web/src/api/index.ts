import axios from './axios'

export interface ApiResponse<T = unknown> {
  code: number
  message: string
  data: T
  timestamp: number
}

export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest {
  username: string
  password: string
  nickname?: string
}

export interface LoginResponse {
  token: string
  userInfo: UserInfo
}

export interface UserInfo {
  id: number
  username: string
  nickname: string
  avatarUrl?: string
  phone?: string
  gender: number
}

export interface UpdateProfileRequest {
  nickname?: string
  avatarUrl?: string
  gender?: number
}

export interface Province {
  id: number
  name: string
  code: string
  description?: string
  sortOrder: number
  isExplored: boolean
}

export interface Food {
  id: number
  provinceId: number
  provinceName: string
  name: string
  description?: string
  ingredients: string[]
  steps: string[]
  coverUrl?: string
  videoUrl?: string
  tags: string[]
  viewCount: number
  likeCount: number
  collectCount: number
  avgRating: number
  isCollected: boolean
}

export interface CollectResult {
  isCollected: boolean
  collectCount: number
}

export interface ExploreProgress {
  provinceId: number
  provinceName: string
  isExplored: boolean
  foodViewedCount: number
}

export interface ExploreStats {
  exploredCount: number
  totalCount: number
  percentage: number
}

export interface ChatSession {
  id: number
  title: string
  type: string
  updatedAt: string
  messageCount: number
}

export interface ChatMessage {
  id: number
  sessionId: number
  role: string
  content: string
  metadata?: Record<string, unknown>
  createdAt: string
}

export interface CreateSessionRequest {
  type?: string
}

export interface SendMessageRequest {
  content: string
}

export const authApi = {
  register(data: RegisterRequest) {
    return axios.post<ApiResponse<LoginResponse>>('/auth/register', data)
  },
  login(data: LoginRequest) {
    return axios.post<ApiResponse<LoginResponse>>('/auth/login', data)
  }
}

export const userApi = {
  getProfile() {
    return axios.get<ApiResponse<UserInfo>>('/user/profile')
  },
  updateProfile(data: UpdateProfileRequest) {
    return axios.put<ApiResponse<void>>('/user/profile', data)
  }
}

export const provinceApi = {
  getProvinces() {
    return axios.get<ApiResponse<Province[]>>('/provinces')
  }
}

export const foodApi = {
  getProvinceFoods(provinceId: number, pageNum = 1, pageSize = 10) {
    return axios.get<ApiResponse<Food[]>>(`/foods/province/${provinceId}`, {
      params: { pageNum, pageSize }
    })
  },
  getFoodDetail(id: number) {
    return axios.get<ApiResponse<Food>>(`/foods/${id}`)
  },
  searchFoods(keyword: string) {
    return axios.get<ApiResponse<Food[]>>('/foods/search', { params: { keyword } })
  },
  toggleCollect(id: number) {
    return axios.post<ApiResponse<CollectResult>>(`/foods/${id}/collect`)
  },
  recordView(id: number) {
    return axios.post<ApiResponse<void>>(`/foods/${id}/view`)
  },
  getRanking(type = 'collect') {
    return axios.get<ApiResponse<Food[]>>('/foods/ranking', { params: { type } })
  }
}

export const exploreApi = {
  getProgress() {
    return axios.get<ApiResponse<ExploreProgress[]>>('/explore/progress')
  },
  getStats() {
    return axios.get<ApiResponse<ExploreStats>>('/explore/stats')
  }
}

export const chatApi = {
  createSession(data?: CreateSessionRequest) {
    return axios.post<ApiResponse<ChatSession>>('/chat/sessions', data || {})
  },
  getSessions(pageNum = 1, pageSize = 10) {
    return axios.get<ApiResponse<ChatSession[]>>('/chat/sessions', {
      params: { pageNum, pageSize }
    })
  },
  getMessages(sessionId: number) {
    return axios.get<ApiResponse<ChatMessage[]>>(`/chat/sessions/${sessionId}/messages`)
  },
  sendMessage(sessionId: number, data: SendMessageRequest) {
    return axios.post(`/chat/sessions/${sessionId}/send`, data, {
      responseType: 'stream'
    })
  },
  deleteSession(sessionId: number) {
    return axios.delete<ApiResponse<void>>(`/chat/sessions/${sessionId}`)
  }
}