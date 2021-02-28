import React, { Component } from "react";
import ButtonWithLoader from "../component/ButtonWithLoader";
import FormInput from "../component/FormInput";

class LoginPage extends Component {
  state = {
    username: "",
    password: "",
    apiError: undefined,
    pendingApiCall: false,
  };
  onChangeUserName = (e) => {
    const value = e.target.value;
    this.setState({
      username: value,
      apiError: undefined,
    });
  };
  onChangePassword = (e) => {
    const value = e.target.value;
    this.setState({
      password: value,
      apiError: undefined,
    });
  };
  onClickLogin = () => {
    const body = {
      username: this.state.username,
      password: this.state.password,
    };
    this.setState({
      pendingApiCall: true,
    });
    this.props.actions
      .postLogin(body)
      .then((response) => {
        this.setState(
          {
            pendingApiCall: false,
          },
          () => {
            this.props.history.push("/");
          }
        );
      })
      .catch((error) => {
        if (error.response) {
          this.setState({
            apiError: error.response.data.message,
          });
        }
        this.setState({
          pendingApiCall: false,
        });
      });
  };
  render() {
    let disabledSubmit = false;
    if (this.state.username === "" || this.state.password === "") {
      disabledSubmit = true;
    }
    return (
      <div className="container">
        <h2
          id="signup-form-header"
          className="center indigo-text text-darken-2"
        >
          Login
        </h2>
        <form className="col s12 m6 offset-m3">
          <FormInput
            placeholder="Your username"
            label="Username"
            id="user-name"
            value={this.state.username}
            onChange={this.onChangeUserName}
          />
          <FormInput
            type="password"
            placeholder="Your password"
            label="Password"
            id="password"
            value={this.state.password}
            onChange={this.onChangePassword}
          />
        </form>
        <div className="center">
          <ButtonWithLoader
            label="Log In"
            pendingApiCall={this.state.pendingApiCall}
            onClickAction={this.onClickLogin}
            disabled={disabledSubmit}
          />
        </div>
        {this.state.apiError && (
          <div className="center">
            <span
              id={"login-error-span"}
              className="helper-text red-text"
              data-error={this.state.apiError}
            >
              {this.state.apiError}
            </span>
          </div>
        )}
      </div>
    );
  }
}

LoginPage.defaultProps = {
  actions: {
    postLogin: () => new Promise((resolve, reject) => resolve({})),
  },
};

export default LoginPage;
