import axios from 'axios';

// Create base axios instance
const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1'
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
  console.log('token', localStorage.getItem('token'));
    const token = localStorage.getItem('token');
    if (token) {
        console.log('auth');
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor
api.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response?.status === 401) {
      // Handle unauthorized access
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;