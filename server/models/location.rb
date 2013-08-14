class Location < ActiveRecord::Base
  belongs_to :room
  belongs_to :user

  def to_json()
    super(methods => [:user])
  end
end
