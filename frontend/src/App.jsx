import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom"
import { AuthProvider, useAuth } from "@/contexts/AuthContext"
import { ThemeProvider } from "@/contexts/ThemeContext"
import { Layout } from "@/components/Layout"
import { Dashboard } from "@/pages/Dashboard"
import { Ingredientes } from "@/pages/Ingredientes"
import { Receitas } from "@/pages/Receitas"
import { Transacoes } from "@/pages/Transacoes"
import { Login } from "@/pages/Login"

function ProtectedRoute({ children }) {
  const { isAuthenticated } = useAuth()
  return isAuthenticated ? children : <Navigate to="/login" replace />
}

function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route
              element={
                <ProtectedRoute>
                  <Layout />
                </ProtectedRoute>
              }
            >
              <Route path="/" element={<Dashboard />} />
              <Route path="/ingredientes" element={<Ingredientes />} />
              <Route path="/receitas" element={<Receitas />} />
              <Route path="/transacoes" element={<Transacoes />} />
            </Route>
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </ThemeProvider>
  )
}

export default App
