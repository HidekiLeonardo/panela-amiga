import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import api from '@/services/api'
import logo from '@/assets/logo.png'

export function Login() {
  const [aba, setAba] = useState('login')
  const [formulario, setFormulario] = useState({ email: '', senha: '' })
  const [erro, setErro] = useState(null)
  const [sucesso, setSucesso] = useState(null)
  const [enviando, setEnviando] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  function handleChange(e) {
    setFormulario((prev) => ({ ...prev, [e.target.name]: e.target.value }))
  }

  function trocarAba(novaAba) {
    setAba(novaAba)
    setErro(null)
    setSucesso(null)
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErro(null)
    setSucesso(null)
    setEnviando(true)
    try {
      if (aba === 'login') {
        const res = await api.post('/auth/login', formulario)
        login(res.data)
        navigate('/')
      } else {
        await api.post('/auth/register', formulario)
        setSucesso('Conta criada com sucesso! Faça login para continuar.')
        trocarAba('login')
      }
    } catch {
      setErro(
        aba === 'login'
          ? 'E-mail ou senha inválidos.'
          : 'Erro ao cadastrar. Tente outro e-mail.'
      )
    } finally {
      setEnviando(false)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-orange-50 via-amber-50 to-orange-100 flex items-center justify-center p-4">
      <div className="w-full max-w-sm">
        <div className="text-center mb-8">
          <img src={logo} alt="Panela Amiga" className="w-20 h-20 object-contain mx-auto drop-shadow-xl" />
          <h1 className="text-2xl font-bold text-stone-900 mt-5">Panela Amiga</h1>
          <p className="text-stone-500 text-sm mt-1">Gestão de cozinha</p>
        </div>

        <div className="bg-white rounded-2xl shadow-xl shadow-stone-200/60 border border-stone-100 p-8">
          <div className="flex rounded-xl bg-stone-100 p-1 mb-6 gap-1">
            {['login', 'register'].map((t) => (
              <button
                key={t}
                onClick={() => trocarAba(t)}
                className={`flex-1 py-2 rounded-lg text-sm font-medium transition-all ${
                  aba === t
                    ? 'bg-white text-stone-900 shadow-sm'
                    : 'text-stone-500 hover:text-stone-700'
                }`}
              >
                {t === 'login' ? 'Entrar' : 'Cadastrar'}
              </button>
            ))}
          </div>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-1.5">
              <label className="text-sm font-medium text-stone-700">E-mail</label>
              <Input
                name="email"
                type="email"
                placeholder="seu@email.com"
                value={formulario.email}
                onChange={handleChange}
                required
                autoComplete="email"
              />
            </div>

            <div className="space-y-1.5">
              <label className="text-sm font-medium text-stone-700">Senha</label>
              <Input
                name="senha"
                type="password"
                placeholder="••••••••"
                value={formulario.senha}
                onChange={handleChange}
                required
                autoComplete={aba === 'login' ? 'current-password' : 'new-password'}
              />
            </div>

            {erro && (
              <div className="text-sm text-red-600 bg-red-50 border border-red-200 rounded-lg px-3 py-2.5">
                {erro}
              </div>
            )}

            {sucesso && (
              <div className="text-sm text-green-700 bg-green-50 border border-green-200 rounded-lg px-3 py-2.5">
                {sucesso}
              </div>
            )}

            <Button type="submit" disabled={enviando} className="w-full mt-2 py-2.5">
              {enviando ? 'Aguarde...' : aba === 'login' ? 'Entrar' : 'Criar conta'}
            </Button>
          </form>
        </div>

        <p className="text-center text-xs text-stone-400 mt-6">
          Panela Amiga © {new Date().getFullYear()}
        </p>
      </div>
    </div>
  )
}
