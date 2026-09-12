/* =========================================================
   PIPELINE STAGES
========================================================= */

const STAGES = {

  "stage-1": {
    num: "STAGE 01",
    title: "Code Quality & Testing",
    desc: "Code quality, linting, unit tests and coverage checks before the code moves forward.",
    tools: ["ESLint", "JUnit", "Jest", "SonarQube"],
    rules: [
      "Lint checks must pass.",
      "Unit tests must pass.",
      "Minimum code coverage should be maintained."
    ],
    failure: "Lint, test or coverage gate failed"
  },

  "stage-2": {
    num: "STAGE 02",
    title: "Security & Dependency Scanning",
    desc: "Security checks for secrets, source code and vulnerable dependencies.",
    tools: ["Gitleaks", "Trivy", "Snyk", "Dependabot"],
    rules: [
      "No secrets or private keys in the repository.",
      "HIGH and CRITICAL vulnerabilities must be reviewed.",
      "Dependency security checks must pass."
    ],
    failure: "Secret or critical vulnerability detected"
  },

  "stage-3": {
    num: "STAGE 03",
    title: "Build & Container Security",
    desc: "Build the application artifact or container image and scan it for security issues.",
    tools: ["Docker", "Buildx", "Trivy", "Syft"],
    rules: [
      "Build must complete successfully.",
      "Container image must pass vulnerability scanning.",
      "Generate an SBOM for the artifact."
    ],
    failure: "Build failed or container vulnerability detected"
  },

  "stage-4": {
    num: "STAGE 04",
    title: "Artifact Signing & Verification",
    desc: "Sign the build artifact and verify its identity and integrity before deployment.",
    tools: ["Cosign", "Sigstore", "GitHub OIDC"],
    rules: [
      "Artifacts should be cryptographically signed.",
      "Use immutable image digests.",
      "Signature verification must pass."
    ],
    failure: "Artifact signing or verification failed"
  },

  "stage-5": {
    num: "STAGE 05",
    title: "Deployment & Environment Validation",
    desc: "Deploy the application to the target environment and validate runtime health.",
    tools: ["Kubernetes", "Helm", "Docker", "Curl"],
    rules: [
      "Deployment must complete successfully.",
      "Health and readiness checks must pass.",
      "Smoke tests must pass."
    ],
    failure: "Deployment or environment health check failed"
  },

  "stage-6": {
    num: "STAGE 06",
    title: "Production Release & Monitoring",
    desc: "Release the verified artifact to production and monitor application health.",
    tools: ["Kubernetes", "ArgoCD", "Azure", "Prometheus"],
    rules: [
      "Only verified artifacts can reach production.",
      "Production health checks must pass.",
      "Application must be monitored after release."
    ],
    failure: "Production release or monitoring check failed"
  }

};


/* =========================================================
   STATE
========================================================= */

let selectedEndpoint = "/api/details";


/* =========================================================
   INITIALIZE
========================================================= */

document.addEventListener("DOMContentLoaded", function () {

  displayStage("stage-1");

  bindStageCards();

  bindEndpointButtons();

  loadInitialDetails();

});


/* =========================================================
   DISPLAY SELECTED STAGE
========================================================= */

function displayStage(stageKey) {

  const stage = STAGES[stageKey];

  if (!stage) {
    return;
  }


  const stageNumber =
    document.getElementById("detailStageNum");

  const title =
    document.getElementById("detailTitle");

  const description =
    document.getElementById("detailDesc");

  const tools =
    document.getElementById("detailTools");

  const rules =
    document.getElementById("detailRules");

  const failure =
    document.getElementById("detailFailure");


  if (
    !stageNumber ||
    !title ||
    !description ||
    !tools ||
    !rules ||
    !failure
  ) {
    console.error("Inspector elements are missing.");
    return;
  }


  stageNumber.textContent =
    stage.num;

  title.textContent =
    stage.title;

  description.textContent =
    stage.desc;


  /* Tools */

  tools.innerHTML = "";

  stage.tools.forEach(function (tool) {

    const tag =
      document.createElement("span");

    tag.className =
      "tool-tag";

    tag.textContent =
      tool;

    tools.appendChild(tag);

  });


  /* Rules */

  rules.innerHTML = "";

  stage.rules.forEach(function (rule) {

    const item =
      document.createElement("li");

    item.textContent =
      rule;

    rules.appendChild(item);

  });


  /* Failure */

  failure.textContent =
    stage.failure;

}


/* =========================================================
   STAGE CARD EVENTS
========================================================= */

function bindStageCards() {

  const cards =
    document.querySelectorAll(".stage-card");


  cards.forEach(function (card) {

    card.addEventListener("click", function () {

      cards.forEach(function (item) {

        item.classList.remove("active");

      });


      card.classList.add("active");


      const stageKey =
        card.getAttribute("data-stage");


      displayStage(stageKey);

    });

  });

}


/* =========================================================
   API TABS + REFRESH
========================================================= */

function bindEndpointButtons() {

  const tabs =
    document.querySelectorAll(".tab-btn");

  const refreshButton =
    document.getElementById("refreshBtn");


  tabs.forEach(function (tab) {

    tab.addEventListener("click", function () {

      tabs.forEach(function (item) {

        item.classList.remove("active");

      });


      tab.classList.add("active");


      selectedEndpoint =
        tab.getAttribute("data-url");


      fetchEndpointData(
        selectedEndpoint
      );

    });

  });


  if (refreshButton) {

    refreshButton.addEventListener(
      "click",
      function () {

        fetchEndpointData(
          selectedEndpoint
        );

      }
    );

  }

}


/* =========================================================
   FETCH API
========================================================= */

function fetchEndpointData(url) {

  const output =
    document.getElementById("endpointOutput");

  const status =
    document.getElementById("endpointStatus");


  if (!output) {
    return;
  }


  output.textContent =
    "Connecting to " + url + "...";


  if (status) {

    status.textContent =
      "CONNECTING...";

  }


  fetch(url)

    .then(function (response) {

      if (!response.ok) {

        throw new Error(
          "HTTP " + response.status
        );

      }

      return response.json();

    })


    .then(function (data) {

      output.textContent =
        JSON.stringify(
          data,
          null,
          2
        );


      if (status) {

        status.textContent =
          "API CONNECTED";

      }

    })


    .catch(function (error) {

      output.textContent =
        "API Error: " +
        error.message;


      if (status) {

        status.textContent =
          "API ERROR";

      }

    });

}


/* =========================================================
   LOAD INITIAL APPLICATION DETAILS
========================================================= */

function loadInitialDetails() {

  const output =
    document.getElementById("endpointOutput");

  const envBadge =
    document.getElementById("envBadge");

  const versionBadge =
    document.getElementById("versionBadge");


  if (output) {

    output.textContent =
      "Loading application details...";

  }


  fetch("/api/details")

    .then(function (response) {

      if (!response.ok) {

        throw new Error(
          "API unavailable"
        );

      }

      return response.json();

    })


    .then(function (data) {


      /* Environment */

      if (
        envBadge &&
        data.environment
      ) {

        envBadge.textContent =
          "ENV: " +
          String(
            data.environment
          ).toUpperCase();

      }


      /* Version */

      if (
        versionBadge &&
        data.version
      ) {

        versionBadge.textContent =
          "VER: " +
          data.version;

      }


      /* API output */

      if (output) {

        output.textContent =
          JSON.stringify(
            data,
            null,
            2
          );

      }


      const status =
        document.getElementById(
          "endpointStatus"
        );


      if (status) {

        status.textContent =
          "API CONNECTED";

      }

    })


    .catch(function (error) {

      console.error(
        "Initial API request failed:",
        error
      );


      fetchEndpointData(
        "/api/details"
      );

    });

}