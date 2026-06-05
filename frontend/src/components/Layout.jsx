import { NavLink, Outlet, useNavigate } from "react-router-dom"
import { LayoutDashboard, Package, BookOpen, DollarSign, LogOut, Sun, Moon } from "lucide-react"
import { useAuth } from "@/contexts/AuthContext"
import { useTheme } from "@/contexts/ThemeContext"
import { cn } from "@/lib/utils"
import logo from "@/assets/logo.png"

const navItems = [
  { to: "/", label: "Dashboard", icon: LayoutDashboard },
  { to: "/ingredientes", label: "Ingredientes", icon: Package },
  { to: "/receitas", label: "Receitas", icon: BookOpen },
  { to: "/transacoes", label: "Transações", icon: DollarSign },
]

function SidebarLogo() {
  return (
    <img
      src={logo}
      alt="Panela Amiga"
      className="w-10 h-10 object-contain rounded-xl"
    />
  )
}

export function Layout() {
  const { logout, email } = useAuth()
  const { isDark, toggle } = useTheme()
  const navigate = useNavigate()

  function handleLogout() {
    logout()
    navigate("/login")
  }

  return (
    <div className="flex min-h-screen bg-background">
      <aside className="w-60 bg-stone-900 flex flex-col flex-shrink-0">
        <div className="p-5 border-b border-stone-700/60">
          <div className="flex items-center gap-3">
            <SidebarLogo />
            <div>
              <p className="text-white font-bold text-sm leading-tight">Panela Amiga</p>
              <p className="text-stone-400 text-xs mt-0.5">Gestão de cozinha</p>
            </div>
          </div>
        </div>

        <nav className="flex-1 p-3 space-y-0.5">
          {navItems.map(({ to, label, icon: Icon }) => (
            <NavLink
              key={to}
              to={to}
              end={to === "/"}
              className={({ isActive }) =>
                cn(
                  "flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-all",
                  isActive
                    ? "bg-orange-500 text-white shadow-md shadow-orange-900/30"
                    : "text-stone-400 hover:bg-stone-800 hover:text-stone-100"
                )
              }
            >
              <Icon size={16} />
              {label}
            </NavLink>
          ))}
        </nav>

        <div className="p-3 border-t border-stone-700/60 space-y-0.5">
          {email && (
            <div className="px-3 py-2 mb-1">
              <p className="text-xs text-stone-500 truncate">{email}</p>
            </div>
          )}
          <button
            onClick={toggle}
            className="flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium w-full text-stone-400 hover:bg-stone-800 hover:text-stone-100 transition-all"
          >
            {isDark ? <Sun size={16} /> : <Moon size={16} />}
            {isDark ? "Modo claro" : "Modo escuro"}
          </button>
          <button
            onClick={handleLogout}
            className="flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium w-full text-stone-400 hover:bg-stone-800 hover:text-stone-100 transition-all"
          >
            <LogOut size={16} />
            Sair
          </button>
        </div>
      </aside>

      <main className="flex-1 overflow-auto">
        <Outlet />
      </main>
    </div>
  )
}
