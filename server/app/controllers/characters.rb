Server::App.controllers :characters do
  get :list, provides: :json do
    Character.all.to_json
  end
end
