require 'securerandom'

class User < ActiveRecord::Base
  before_create :generate_token
  has_many :users
  has_many :locations
  has_many :territories

  def valid_territories
    territories.where :expired_time => nil
  end

  def generate_token
    token = SecureRandom.base64(16)
    self.token = token
  end

  def cache_key
    "user_cache_key=#{self.id}"
  end

  def add_location(latitude, longitude)
    Location.new(latitude: latitude, longitude: longitude){|loc|
      loc.user = self
    }
  end
end
