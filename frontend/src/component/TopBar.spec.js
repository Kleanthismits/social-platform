import React from "react";
import { render } from "@testing-library/react";
import TopBar from "./TopBar";
import { MemoryRouter } from "react-router-dom";

const setup = () => {
  return render(
    <MemoryRouter>
      <TopBar />
    </MemoryRouter>
  );
};
describe("TopBar", () => {
  describe("Layout", () => {
    it("has application logo", () => {
      const { container } = setup();
      expect(container.querySelector("img").src).toContain("chatty-logo2.png");
    });

    it("has link to home from logo", () => {
      const { container } = setup();
      expect(
        container.querySelector("img").parentElement.getAttribute("href")
      ).toBe("/");
    });
    it("has link to signup", () => {
      const { queryByText } = setup();
      expect(queryByText("Sign Up").getAttribute("href")).toBe(
        "/signup"
      );
    });
    it("has link to login", () => {
        const { queryByText } = setup();
        expect(queryByText("Login").getAttribute("href")).toBe(
          "/login"
        );
      });
  });
});
