import React from "react";
import { render, fireEvent, waitFor } from "@testing-library/react";
import LoginPage from "./LoginPage";

describe("Login Page", () => {
  describe("Layout", () => {
    it("has header of Login", () => {
      const { container } = render(<LoginPage />);
      const header = container.querySelector("h2");
      expect(header).toHaveTextContent("Login");
    });
    it("has input for username", () => {
      const { queryByPlaceholderText } = render(<LoginPage />);
      const username = queryByPlaceholderText("Your username");
      expect(username).toBeInTheDocument();
    });
    it("has input for password", () => {
      const { queryByPlaceholderText } = render(<LoginPage />);
      const passwordInput = queryByPlaceholderText("Your password");
      expect(passwordInput).toBeInTheDocument();
    });
    it("has password type for password input", () => {
      const { queryByPlaceholderText } = render(<LoginPage />);
      const passwordInput = queryByPlaceholderText("Your password");
      expect(passwordInput.type).toBe("password");
    });
    it("has login button", () => {
      const { container } = render(<LoginPage />);
      const button = container.querySelector("button");
      expect(button).toBeInTheDocument();
    });
  });
  describe("interactions", () => {
    const changeEvent = (content) => {
      return {
        target: {
          value: content,
        },
      };
    };

    const mockAsyncDelayed = () => {
      return jest.fn().mockImplementation(() => {
        return new Promise((resolve, reject) => {
          setTimeout(() => {
            resolve({});
          }, 300);
        });
      });
    };
    let usernameInput, passwordInput, button;

    const setupForSubmit = (props) => {
      const rendered = render(<LoginPage {...props} />);

      const { container, queryByPlaceholderText } = rendered;
      usernameInput = queryByPlaceholderText("Your username");
      fireEvent.change(usernameInput, changeEvent("my-user-name"));
      passwordInput = queryByPlaceholderText("Your password");
      fireEvent.change(passwordInput, changeEvent("P@ssw0rd"));
      button = container.querySelector("button");
      return rendered;
    };
    it("sets the username into state", () => {
      const { queryByPlaceholderText } = render(<LoginPage />);
      const username = queryByPlaceholderText("Your username");
      fireEvent.change(username, changeEvent("my-user-name"));
      expect(username).toHaveValue("my-user-name");
    });

    it("sets the password into state", () => {
      const { queryByPlaceholderText } = render(<LoginPage />);
      const passwordInput = queryByPlaceholderText("Your password");
      fireEvent.change(passwordInput, changeEvent("P@ssw0rd"));
      expect(passwordInput).toHaveValue("P@ssw0rd");
    });
    it("calls postLogin when actions are provided as props and input fields have value", () => {
      const actions = {
        postLogin: jest.fn().mockResolvedValue({}),
      };
      setupForSubmit({ actions });
      fireEvent.click(button);
      expect(actions.postLogin).toHaveBeenCalledTimes(1);
    });
    it("does not throw exception when clicking the button when actions are not provided in props", () => {
      setupForSubmit();
      expect(() => fireEvent.click(button)).not.toThrow();
    });
    it("calls postLogin with credentials in body", () => {
      const actions = {
        postLogin: jest.fn().mockResolvedValue({}),
      };
      const expectedUserObject = {
        username: "my-user-name",
        password: "P@ssw0rd",
      };
      setupForSubmit({ actions });
      fireEvent.click(button);
      expect(actions.postLogin).toHaveBeenCalledWith(expectedUserObject);
    });
    it("enables the button when username is not empty", () => {
      setupForSubmit();
      expect(button).not.toBeDisabled();
    });
    it("disables the button when username is empty", () => {
      setupForSubmit();
      fireEvent.change(usernameInput, changeEvent(""));
      expect(button).toBeDisabled();
    });
    it("enables the button when password is not empty", () => {
      setupForSubmit();
      expect(button).not.toBeDisabled();
    });
    it("disables the button when password is empty", () => {
      setupForSubmit();
      fireEvent.change(password, changeEvent(""));
      expect(button).toBeDisabled();
    });
    it("displays login error when login fails", async () => {
      const actions = {
        postLogin: jest.fn().mockRejectedValue({
          response: {
            data: {
              message: "Login failed",
            },
          },
        }),
      };
      const { queryByText } = setupForSubmit({ actions });
      fireEvent.click(button);
      await waitFor(() =>
        expect(queryByText("Login failed")).toBeInTheDocument()
      );
    });
    it("clears login error when user changes username", async () => {
      const actions = {
        postLogin: jest.fn().mockRejectedValue({
          response: {
            data: {
              message: "Login failed",
            },
          },
        }),
      };
      const { queryByText } = setupForSubmit({ actions });
      fireEvent.click(button);
      await waitFor(() => queryByText("Login failed"));
      fireEvent.change(usernameInput, changeEvent("updated-username"));
      expect(queryByText("Login failed")).not.toBeInTheDocument();
    });
    it("clears login error when user changes password", async () => {
      const actions = {
        postLogin: jest.fn().mockRejectedValue({
          response: {
            data: {
              message: "Login failed",
            },
          },
        }),
      };
      const { queryByText } = setupForSubmit({ actions });
      fireEvent.click(button);
      await waitFor(() => queryByText("Login failed"));
      fireEvent.change(passwordInput, changeEvent("updated-password"));
      expect(queryByText("Login failed")).not.toBeInTheDocument();
    });
    it("does not allow user to click the login button when there is an ongoing api call", () => {
      const actions = {
        postLogin: mockAsyncDelayed(),
      };
      setupForSubmit({ actions });

      fireEvent.click(button);

      fireEvent.click(button);

      expect(actions.postLogin).toHaveBeenCalledTimes(1);
    });
    it("displays spinner when there is an ongoing api call", () => {
      const actions = {
        postLogin: mockAsyncDelayed(),
      };
      const { container } = setupForSubmit({ actions });

      fireEvent.click(button);

      const spinner = container.querySelector("#spinner-loading");
      expect(spinner).toBeInTheDocument();
    });
    it("hides spinner after api call finishes successfully", async () => {
      const actions = {
        postLogin: mockAsyncDelayed(),
      };
      const { container } = setupForSubmit({ actions });

      fireEvent.click(button);

      await waitFor(() =>
        expect(
          container.querySelector("#spinner-loading")
        ).not.toBeInTheDocument()
      );
    });
    it("hides spinner after api call finishes with error", async () => {
      const actions = {
        postLogin: jest.fn().mockImplementation(() => {
          return new Promise((resolve, reject) => {
            setTimeout(() => {
              reject({
                response: { data: {} },
              });
            }, 300);
          });
        }),
      };
      const { container } = setupForSubmit({ actions });

      fireEvent.click(button);

      await waitFor(() =>
        expect(
          container.querySelector("#spinner-loading")
        ).not.toBeInTheDocument()
      );
    });
    it("redirects to homepage after successfull login", async () => {
      const actions = {
        postLogin: jest.fn().mockResolvedValue({}),
      };
      const history = {
        push: jest.fn(),
      };
      setupForSubmit({ actions, history });
      fireEvent.click(button);

      await waitFor(() => expect(history.push).toHaveBeenCalledWith("/"));
    });
  });
});

console.error = () => {};
