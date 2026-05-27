import { NavLink, Outlet } from "react-router-dom"
import { LayoutDashboard, Package, BookOpen, DollarSign } from "lucide-react"
import { cn } from "@/lib/utils"

const navItems = [
  { to: "/", label: "Dashboard", icon: LayoutDashboard },
  { to: "/ingredientes", label: "Ingredientes", icon: Package },
  { to: "/receitas", label: "Receitas", icon: BookOpen },
  { to: "/transacoes", label: "Transações", icon: DollarSign },
]

export function Layout() {
  return (
    <div className="flex min-h-screen bg-background">
      <aside className="w-56 border-r bg-card flex flex-col">
        <div className="p-6 border-b">
          <h1 className="text-lg font-bold text-primary">Panela Amiga</h1>
          <p className="text-xs text-muted-foreground mt-0.5">Gestão de cozinha</p>
        </div>
        <nav className="flex-1 p-3 space-y-1">
          {navItems.map(({ to, label, icon: Icon }) => (
            <NavLink
              key={to}
              to={to}
              end={to === "/"}
              className={({ isActive }) =>
                cn(
                  "flex items-center gap-3 rounded-lg px-3 py-2 text-sm transition-colors",
                  isActive
                    ? "bg-primary text-primary-foreground"
                    : "text-muted-foreground hover:bg-accent hover:text-accent-foreground"
                )
              }
            >
              <Icon size={16} />
              {label}
            </NavLink>
          ))}
        </nav>
      </aside>

      <main className="flex-1 overflow-auto">
        <Outlet />
      </main>
    </div>
  )
}
