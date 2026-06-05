import { createContext, useContext, useState } from 'react'

const AuthContext = createContext(null)

function decodeEmail(token) {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    return payload.sub ?? null
  } catch {
    return null
  }
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('token'))

  const login = (newToken) => {
    localStorage.setItem('token', newToken)
    setToken(newToken)
  }

  const logout = () => {
    localStorage.removeItem('token')
    setToken(null)
  }

  const email = token ? decodeEmail(token) : null

  return (
    <AuthContext.Provider value={{ token, login, logout, isAuthenticated: !!token, email }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)
