import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { AuthProvider } from "../context/authContext";
import LoginPage from "../pages/loginPage";
import apiClient from "../api/apiClient";

const mockNavigate = jest.fn();

jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => mockNavigate,
}));

const renderLoginPage = () => {
  return render(
    <MemoryRouter>
      <AuthProvider>
        <LoginPage />
      </AuthProvider>
    </MemoryRouter>
  );
};

describe("LoginPage", () => {

  beforeEach(() => {
    jest.clearAllMocks();
    sessionStorage.clear();
    jest.restoreAllMocks();
  });

  test("renders login form correctly", () => {
    renderLoginPage();
    expect(screen.getByText("PayFlow")).toBeInTheDocument();
    expect(screen.getByText("Sign in to your account")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Enter username")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Enter password")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Sign In" })).toBeInTheDocument();
  });

  test("user can type in username and password fields", () => {
    renderLoginPage();
    const usernameInput = screen.getByPlaceholderText("Enter username");
    const passwordInput = screen.getByPlaceholderText("Enter password");
    fireEvent.change(usernameInput, { target: { value: "admin" } });
    fireEvent.change(passwordInput, { target: { value: "admin123" } });
    expect(usernameInput).toHaveValue("admin");
    expect(passwordInput).toHaveValue("admin123");
  });

  test("shows loading state while submitting", async () => {
    jest.spyOn(apiClient, "post").mockImplementation(
      () => new Promise(() => {})
    );
    renderLoginPage();
    fireEvent.change(screen.getByPlaceholderText("Enter username"), {
      target: { value: "admin" },
    });
    fireEvent.change(screen.getByPlaceholderText("Enter password"), {
      target: { value: "admin123" },
    });
    fireEvent.submit(screen.getByRole("button", { name: "Sign In" }));
    await waitFor(() => {
      expect(
        screen.getByRole("button", { name: "Signing in..." })
      ).toBeDisabled();
    });
  });

  test("successful login redirects to dashboard", async () => {
    jest.spyOn(apiClient, "post").mockResolvedValue({
      data: { message: "Login successful", data: "fake-jwt-token" },
    });
    renderLoginPage();
    fireEvent.change(screen.getByPlaceholderText("Enter username"), {
      target: { value: "admin" },
    });
    fireEvent.change(screen.getByPlaceholderText("Enter password"), {
      target: { value: "admin123" },
    });
    fireEvent.submit(screen.getByRole("button", { name: "Sign In" }));
    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith("/dashboard");
    });
  });

  test("shows error on invalid credentials", async () => {
    jest.spyOn(apiClient, "post").mockRejectedValue({
      response: { status: 401 },
    });
    renderLoginPage();
    fireEvent.change(screen.getByPlaceholderText("Enter username"), {
      target: { value: "admin" },
    });
    fireEvent.change(screen.getByPlaceholderText("Enter password"), {
      target: { value: "wrongpassword" },
    });
    fireEvent.submit(screen.getByRole("button", { name: "Sign In" }));
    await waitFor(() => {
      expect(
        screen.getByText("Invalid username or password")
      ).toBeInTheDocument();
    });
  });

  test("shows generic error on server failure", async () => {
    jest.spyOn(apiClient, "post").mockRejectedValue({
      response: { status: 500 },
    });
    renderLoginPage();
    fireEvent.change(screen.getByPlaceholderText("Enter username"), {
      target: { value: "admin" },
    });
    fireEvent.change(screen.getByPlaceholderText("Enter password"), {
      target: { value: "admin123" },
    });
    fireEvent.submit(screen.getByRole("button", { name: "Sign In" }));
    await waitFor(() => {
      expect(
        screen.getByText("Something went wrong. Please try again.")
      ).toBeInTheDocument();
    });
  });

  test("stores token in sessionStorage after successful login", async () => {
    jest.spyOn(apiClient, "post").mockResolvedValue({
      data: { message: "Login successful", data: "fake-jwt-token" },
    });
    renderLoginPage();
    fireEvent.change(screen.getByPlaceholderText("Enter username"), {
      target: { value: "admin" },
    });
    fireEvent.change(screen.getByPlaceholderText("Enter password"), {
      target: { value: "admin123" },
    });
    fireEvent.submit(screen.getByRole("button", { name: "Sign In" }));
    await waitFor(() => {
      expect(sessionStorage.getItem("token")).toBe("fake-jwt-token");
    });
  });
});