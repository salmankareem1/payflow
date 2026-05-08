import { createContext, useContext, useState } from "react";

interface AuthContextType{
    token:string|null;
    isAuthenticated:boolean;
    login:(token:string)=> void;
    logout: ()=>void;
}

const AuthContext= createContext<AuthContextType |null>(null)

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [token, setToken] = useState<string | null>(
    sessionStorage.getItem("token")
  );

  const login=(newToken:string)=>{
    sessionStorage.setItem("token", newToken);
    setToken(newToken);
  }

  const logout=()=>{
    sessionStorage.removeItem("token")
    setToken(null)
}

return(
    <AuthContext.Provider
    value={
        {
            token,
            isAuthenticated:!!token,
            login,
            logout
        }
    }>
    {children}</AuthContext.Provider>
)
}

export function useAuth(){
    const context= useContext(AuthContext);
    if(!context){
        throw new Error("useAuth must be used within an AuthProvider");

    }
    return context;
}