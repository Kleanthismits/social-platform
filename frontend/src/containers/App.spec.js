import React from "react";
import { fireEvent, render } from "@testing-library/react";
import App from "./App";
import { MemoryRouter } from "react-router-dom";

const setup = (path) => {
  return render(
    <MemoryRouter initialEntries={[path]}>
      <App />
    </MemoryRouter>
  );
};
describe("App", () => {
  it("displays homepage when url is /", () => {
    const { queryByTestId } = setup("/");
    expect(queryByTestId("homepage")).toBeInTheDocument();
  });
  it("displays loginpage when url is /login", () => {
    const { container } = setup("/login");
    expect(container.querySelector("h2")).toHaveTextContent("Login");
  });
  it("displays only loginpage when url is /login", () => {
    const { queryByTestId } = setup("/login");
    expect(queryByTestId("homepage")).not.toBeInTheDocument();
  });
  it("displays UserSignUpPage when url is /signup", () => {
    const { container } = setup("/signup");
    expect(container.querySelector("h2")).toHaveTextContent("Sign Up");
  });
  it("displays userpage when url is other than /, /login, /signup", () => {
    const { queryByTestId } = setup("/user1");
    expect(queryByTestId("userpage")).toBeInTheDocument();
  });
  it("displays topBar when url is /", () => {
    const { container } = setup("/");
    expect(container.querySelector("nav")).toBeInTheDocument();
  });
  it("displays topBar when url is /signup", () => {
    const { container } = setup("/signup");
    expect(container.querySelector("nav")).toBeInTheDocument();
  });
  it("displays topBar when url is /login", () => {
    const { container } = setup("/login");
    expect(container.querySelector("nav")).toBeInTheDocument();
  });
  it("displays topBar when url is /user1", () => {
    const { container } = setup("/user1");
    expect(container.querySelector("nav")).toBeInTheDocument();
  });
  it("shows the user signup page when clicking signup", () => {
    const { queryByText, container } = setup("/");
    fireEvent.click(queryByText("Sign Up"));
    expect(container.querySelector("h2")).toHaveTextContent("Sign Up");
  });
  it("shows the login page when clicking login", () => {
    const { queryByText, container } = setup("/");
    fireEvent.click(queryByText("Login"));
    expect(container.querySelector("h2")).toHaveTextContent("Login");
  });
  it("shows the home page when clicking the logo", () => {
    const { queryByTestId, container } = setup("/login");
    fireEvent.click(container.querySelector("img"));
    expect(queryByTestId("homepage")).toBeInTheDocument();
  });
});
