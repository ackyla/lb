require 'securerandom'

class User < ActiveRecord::Base
  before_create :generate_token
  has_many :users
  has_many :locations
  has_many :my_territories, class_name: "Territory", foreign_key: 'owner_id'
  has_many :invasions
  has_many :enemy_territories, class_name: "Territory", through: :invasions, source: :territory
  has_many :notifications

  def valid_territories
    my_territories.where :expired_time => nil
  end

  def generate_token
    token = SecureRandom.base64(16)
    self.token = token
  end

  def add_location(latitude, longitude)
    Location.new(latitude: latitude, longitude: longitude){|loc|
      loc.user = self
    }
  end

  def to_hash
    hash = Hash[self.attributes]
    hash["user_id"] = self.id
    hash.delete "id"
    hash
  end
end
