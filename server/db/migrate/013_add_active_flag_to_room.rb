class AddActiveFlagToRoom < ActiveRecord::Migration
  def self.up
    change_table :rooms do |t|
      t.boolean :active, :default => false
    end
  end

  def self.down
    change_table :rooms do |t|
      t.remove :active
    end
  end
end
