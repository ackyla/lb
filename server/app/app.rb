module Server
  class App < Padrino::Application
    use ActiveRecord::ConnectionAdapters::ConnectionManagement
    register Padrino::Rendering
    register Padrino::Mailer
    register Padrino::Helpers

    enable :sessions

    ##
    # Caching support
    #
    # register Padrino::Cache
    # enable :caching
    #
    # You can customize caching store engines:
    #
    # set :cache, Padrino::Cache::Store::Memcache.new(::Memcached.new('127.0.0.1:11211', :exception_retry_limit => 1))
    # set :cache, Padrino::Cache::Store::Memcache.new(::Dalli::Client.new('127.0.0.1:11211', :exception_retry_limit => 1))
    # set :cache, Padrino::Cache::Store::Redis.new(::Redis.new(:host => '127.0.0.1', :port => 6379, :db => 0))
    # set :cache, Padrino::Cache::Store::Memory.new(50)
    # set :cache, Padrino::Cache::Store::File.new(Padrino.root('tmp', app_name.to_s, 'cache')) # default choice
    #

    ##
    # Application configuration options
    #
    # set :raise_errors, true       # Raise exceptions (will stop application) (default for test)
    # set :dump_errors, true        # Exception backtraces are written to STDERR (default for production/development)
    # set :show_exceptions, true    # Shows a stack trace in browser (default for development)
    # set :logging, true            # Logging in STDOUT for development and file for production (default only for development)
    # set :public_folder, 'foo/bar' # Location for static assets (default root/public)
    # set :reload, false            # Reload application files (default in development)
    # set :default_builder, 'foo'   # Set a custom form builder (default 'StandardFormBuilder')
    # set :locale_path, 'bar'       # Set path for I18n translations (default your_apps_root_path/locale)
    # disable :sessions             # Disabled sessions by default (enable if needed)
    # disable :flash                # Disables sinatra-flash (enabled by default if Sinatra::Flash is defined)
    # layout  :my_layout            # Layout can be in views/layouts/foo.ext or views/foo.ext (default :application)
    #

    ##
    # You can configure for a specified environment like:
    #
    #   configure :development do
    #     set :foo, :bar
    #     disable :asset_stamp # no asset timestamping for dev
    #   end
    #
    configure :development do
      register Padrino::Cache
      enable :caching
      set :raise_errors, true       # Raise exceptions (will stop application) (default for test)
      set :dump_errors, true        # Exception backtraces are written to STDERR (default for production/development)
      set :show_exceptions, true    # Shows a stack trace in browser (default for development)
      set :logging, true            # Logging in STDOUT for development and file for production (default only for development)
    end
    ##
    # You can manage errors like:
    #
    #   error 404 do
    #     render 'errors/404'
    #   end
    #
    #   error 505 do
    #     render 'errors/505'
    #   end
    #

    helpers do
      def error_message(code, message)
        content_type :json
        halt 500, {error_code: code, message: message}.to_json
      end

      def invalid_param_error
        error_message(100, "INVALID PARAMETER")
      end

      def find_room(params)
        invalid_param_error unless params.key? "room_id"
        @room ||= Room.find_by_id(params[:room_id])
        error_message(300, "INVALID ROOM") unless @room
      end

      def login(params)
        invalid_param_error unless params.key? "user_id" and params.key? "token"
        @user = User.find_by_id_and_token(params[:user_id], params[:token])
        error_message(200, "INVALID USER") unless @user
      end


      def user_cache_key
        "user_cache?user_id=#{params[:user_id]}"
      end

      def room_cache_key
        "room_cache?room_id=#{params[:room_id]}"
      end
    end

    get '/' do
      'Welcome to LB'
    end
  end
end
