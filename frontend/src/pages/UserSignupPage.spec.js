import React from "react";
import { render, fireEvent, waitFor } from "@testing-library/react";
import UserSignupPage from "./UserSignupPage";

describe("UserSignupPage", () => {
  describe("layout", () => {
    it("has header of Sign up", () => {
      const { container } = render(<UserSignupPage />);
      const header = container.querySelector("#signup-form-header");
      expect(header).toHaveTextContent("Sign Up");
    });
    it("has input for display name", () => {
      const { queryByPlaceholderText } = render(<UserSignupPage />);
      const displayNameInput = queryByPlaceholderText("Your display name");
      expect(displayNameInput).toBeInTheDocument();
    });
    it("has input for username", () => {
      const { queryByPlaceholderText } = render(<UserSignupPage />);
      const userameInput = queryByPlaceholderText("Your username");
      expect(userameInput).toBeInTheDocument();
    });
    it("has input for password", () => {
      const { queryByPlaceholderText } = render(<UserSignupPage />);
      const passwordInput = queryByPlaceholderText("Your password");
      expect(passwordInput).toBeInTheDocument();
    });
    it("has password type for password input", () => {
      const { queryByPlaceholderText } = render(<UserSignupPage />);
      const passwordInput = queryByPlaceholderText("Your password");
      expect(passwordInput.type).toBe("password");
    });
    it("has input for password repeat", () => {
      const { queryByPlaceholderText } = render(<UserSignupPage />);
      const passwordRepeatInput = queryByPlaceholderText(
        "Repeat your password"
      );
      expect(passwordRepeatInput).toBeInTheDocument();
    });
    it("has password type for password repeat input", () => {
      const { queryByPlaceholderText } = render(<UserSignupPage />);
      const passwordRepeatInput = queryByPlaceholderText(
        "Repeat your password"
      );
      expect(passwordRepeatInput.type).toBe("password");
    });
    it("has submit button", () => {
      const { container } = render(<UserSignupPage />);
      const button = container.querySelector("button");
      expect(button).toBeInTheDocument();
    });
  });
  describe("Interactions", () => {
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

    let button, displayNameInput, usernameInput, passwordInput, passRepeatInput;

    const setUpForSubmit = (props) => {
      const rendered = render(<UserSignupPage {...props} />);

      const { container, queryByPlaceholderText } = rendered;
      displayNameInput = queryByPlaceholderText("Your display name");
      usernameInput = queryByPlaceholderText("Your username");
      passwordInput = queryByPlaceholderText("Your password");
      passRepeatInput = queryByPlaceholderText("Repeat your password");

      fireEvent.change(displayNameInput, changeEvent("my-display-name"));
      fireEvent.change(usernameInput, changeEvent("my-username"));
      fireEvent.change(passwordInput, changeEvent("P4ssword"));
      fireEvent.change(passRepeatInput, changeEvent("P4ssword"));

      button = container.querySelector("button");
      return rendered;
    };
    it("sets the display value into state", () => {
      const { queryByPlaceholderText } = render(<UserSignupPage />);
      const displayNameInput = queryByPlaceholderText("Your display name");

      fireEvent.change(displayNameInput, changeEvent("my-display-name"));
      expect(displayNameInput).toHaveValue("my-display-name");
    });
    it("sets the username value into state", () => {
      const { queryByPlaceholderText } = render(<UserSignupPage />);
      const usernameInput = queryByPlaceholderText("Your username");

      fireEvent.change(usernameInput, changeEvent("my-username"));
      expect(usernameInput).toHaveValue("my-username");
    });
    it("sets the password value into state", () => {
      const { queryByPlaceholderText } = render(<UserSignupPage />);
      const passwordInput = queryByPlaceholderText("Your password");

      fireEvent.change(passwordInput, changeEvent("P4ssword"));
      expect(passwordInput).toHaveValue("P4ssword");
    });
    it("sets the password repeat value into state", () => {
      const { queryByPlaceholderText } = render(<UserSignupPage />);
      const passRepeatInput = queryByPlaceholderText("Repeat your password");

      fireEvent.change(passRepeatInput, changeEvent("P4ssword"));
      expect(passRepeatInput).toHaveValue("P4ssword");
    });
    it("calls postSignUp when the fields are valid and the actions are provided in props", () => {
      const actions = {
        postSignUp: jest.fn().mockResolvedValueOnce({}),
      };
      setUpForSubmit({ actions });
      fireEvent.click(button);
      expect(actions.postSignUp).toHaveBeenCalledTimes(1);
    });
    it("does not throw exception when clicking the button when actions is not provided", () => {
      setUpForSubmit();
      expect(() => fireEvent.click(button)).not.toThrow();
    });
    it("calls post with the user body when the fields are valid", () => {
      const actions = {
        postSignUp: jest.fn().mockResolvedValueOnce({}),
      };
      setUpForSubmit({ actions });
      fireEvent.click(button);
      const expectedUserObject = {
        username: "my-username",
        displayName: "my-display-name",
        password: "P4ssword",
      };
      expect(actions.postSignUp).toHaveBeenCalledWith(expectedUserObject);
    });
    it("does not allow user to click the signup button when there is an ongoing api call", () => {
      const actions = {
        postSignUp: mockAsyncDelayed(),
      };
      setUpForSubmit({ actions });

      fireEvent.click(button);

      fireEvent.click(button);

      expect(actions.postSignUp).toHaveBeenCalledTimes(1);
    });
    it("displays spinner when there is an ongoing api call", () => {
      const actions = {
        postSignUp: mockAsyncDelayed(),
      };
      const { container } = setUpForSubmit({ actions });

      fireEvent.click(button);

      const spinner = container.querySelector("#spinner-loading");
      expect(spinner).toBeInTheDocument();
    });
    it("hides spinner after api call finishes successfully", async () => {
      const actions = {
        postSignUp: mockAsyncDelayed(),
      };
      const { container } = setUpForSubmit({ actions });

      fireEvent.click(button);

      await waitFor(() =>
        expect(
          container.querySelector("#spinner-loading")
        ).not.toBeInTheDocument()
      );
    });
    it("hides spinner after api call finishes with error", async () => {
      const actions = {
        postSignUp: jest.fn().mockImplementation(() => {
          return new Promise((resolve, reject) => {
            setTimeout(() => {
              reject({
                response: { data: {} },
              });
            }, 300);
          });
        }),
      };
      const { container } = setUpForSubmit({ actions });

      fireEvent.click(button);

      await waitFor(() =>
        expect(
          container.querySelector("#spinner-loading")
        ).not.toBeInTheDocument()
      );
    });

    it("displays validation error for display name when error is received for the field", async () => {
      const actions = {
        postSignUp: jest.fn().mockImplementation(() => {
          return new Promise((resolve, reject) => {
            setTimeout(() => {
              reject({
                response: {
                  data: {
                    validationErrors: {
                      displayName: "Cannot be null",
                    },
                  },
                },
              });
            }, 300);
          });
        }),
      };
      const { container } = setUpForSubmit({ actions });
      fireEvent.click(button);

      await waitFor(() =>
        expect(
          container.querySelector("#display-name-error-span")
        ).toBeInTheDocument()
      );
    });
    it("enables the signup button when password and repeat-password have same value", () => {
      setUpForSubmit();
      expect(button).not.toBeDisabled();
    });

    it("disables the signup button when repeat-password does not match password", () => {
      setUpForSubmit();
      fireEvent.change(passRepeatInput, changeEvent("new-pass"));
      expect(button).toBeDisabled();
    });

    it("disables the signup button when password does not match repeat-password", () => {
      setUpForSubmit();
      fireEvent.change(passwordInput, changeEvent("new-pass"));
      expect(button).toBeDisabled();
    });

    it("displays error style for password repeat input when password repeat mismatch", () => {
      const { queryByText } = setUpForSubmit();
      fireEvent.change(passRepeatInput, changeEvent("new-pass"));
      const mismatch = queryByText("Does not match to password");
      expect(mismatch).toBeInTheDocument();
    });
    it("displays error style for password repeat input when password input  mismatch", () => {
      const { queryByText } = setUpForSubmit();
      fireEvent.change(passwordInput, changeEvent("new-pass"));
      const mismatch = queryByText("Does not match to password");
      expect(mismatch).toBeInTheDocument();
    });
    it("hides the validation error when user changes the content of the dispalyName", async () => {
      const actions = {
        postSignUp: jest.fn().mockImplementation(() => {
          return new Promise((resolve, reject) => {
            setTimeout(() => {
              reject({
                response: {
                  data: {
                    validationErrors: {
                      displayName: "Cannot be null",
                    },
                  },
                },
              });
            }, 300);
          });
        }),
      };
      const { container } = setUpForSubmit({ actions });
      fireEvent.click(button);

      await waitFor(() =>
        expect(
          container.querySelector("#display-name-error-span")
        ).toBeInTheDocument()
      );

      fireEvent.change(displayNameInput, changeEvent("name updated"));
      const errorSpan = container.querySelector("#display-name-error-span");
      expect(errorSpan).not.toBeInTheDocument();
    });

    it("hides the validation error when user changes the content of the username", async () => {
      const actions = {
        postSignUp: jest.fn().mockImplementation(() => {
          return new Promise((resolve, reject) => {
            setTimeout(() => {
              reject({
                response: {
                  data: {
                    validationErrors: {
                      username: "Cannot be null",
                    },
                  },
                },
              });
            }, 300);
          });
        }),
      };
      const { container } = setUpForSubmit({ actions });
      fireEvent.click(button);

      await waitFor(() =>
        expect(
          container.querySelector("#user-name-error-span")
        ).toBeInTheDocument()
      );

      fireEvent.change(usernameInput, changeEvent("name updated"));
      const errorSpan = container.querySelector("#user-name-error-span");
      expect(errorSpan).not.toBeInTheDocument();
    });

 it("hides the validation error when user changes the content of the password", async () => {
      const actions = {
        postSignUp: jest.fn().mockImplementation(() => {
          return new Promise((resolve, reject) => {
            setTimeout(() => {
              reject({
                response: {
                  data: {
                    validationErrors: {
                      password: "Cannot be null",
                    },
                  },
                },
              });
            }, 300);
          });
        }),
      };
      const { container } = setUpForSubmit({ actions });
      fireEvent.click(button);

      await waitFor(() =>
        expect(
          container.querySelector("#password-error-span")
        ).toBeInTheDocument()
      );

      fireEvent.change(passwordInput, changeEvent("password updated"));
      const errorSpan = container.querySelector("#password-error-span");
      expect(errorSpan).not.toBeInTheDocument();
    });
    it("redirects to homepage after successfull signup", async () => {
      const actions = {
        postSignUp: jest.fn().mockResolvedValue({}),
      };
      const history = {
        push: jest.fn(),
      };
      setUpForSubmit({ actions, history });
      fireEvent.click(button);

      await waitFor(() => expect(history.push).toHaveBeenCalledWith("/"));
    });
    
  });
});

console.error = () => {};
