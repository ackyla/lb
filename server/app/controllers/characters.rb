Server::App.controllers :characters do

  get :index do
    render 'characters/show'
  end

  get :create do
    @character = Character.new
    @character = Character.find(params[:character_id]) if params[:character_id]
    render 'characters/create'
  end

  post :create do
    cid = params[:character]["id"]
    if cid
      params[:character].delete "id"
      @character = Character.update(cid, params[:character])
    else
      @character = Character.new(params[:character])
    end

    if @character.save
      redirect_to url :characters, :index
    else
      render 'characters/create'
    end
  end
  # get :index, :map => '/foo/bar' do
  #   session[:foo] = 'bar'
  #   render 'index'
  # end

  # get :sample, :map => '/sample/url', :provides => [:any, :js] do
  #   case content_type
  #     when :js then ...
  #     else ...
  # end

  # get :foo, :with => :id do
  #   'Maps to url '/foo/#{params[:id]}''
  # end

  # get '/example' do
  #   'Hello world!'
  # end


end
